package main;

import model.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.Duration;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Set<Task> prioritizedTasks = new TreeSet<>(
            (t1, t2) -> {
                if (t1.getStartTime() == null && t2.getStartTime() == null) {
                    return Integer.compare(t1.getId(), t2.getId());
                }
                if (t1.getStartTime() == null) return 1;
                if (t2.getStartTime() == null) return -1;
                int timeCompare = t1.getStartTime().compareTo(t2.getStartTime());
                return timeCompare != 0 ? timeCompare : Integer.compare(t1.getId(), t2.getId());
            }
    );

    // таски
    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task getTask(int id, boolean addToHistory) {
        Task task = tasks.get(id);
        if (task != null && addToHistory) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void addTask(Task task) {
        if (task == null) return;
        if (isTaskOverlapping(task)) {
            throw new ManagerSaveException("Задача пересекается по времени с существующей");
        }
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task == null || !tasks.containsKey(task.getId())) return;
        if (isTaskOverlapping(task)) {
            throw new ManagerSaveException("Задача пересекается по времени с существующей");
        }
        prioritizedTasks.remove(tasks.get(task.getId()));
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void deleteTaskById(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.values().forEach(task -> {
            prioritizedTasks.remove(task);
            historyManager.remove(task.getId());
        });
        tasks.clear();
    }

    //сабтаски
    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (subtask == null) return;
        if (isTaskOverlapping(subtask)) {
            throw new ManagerSaveException("Подзадача пересекается по времени с существующей");
        }
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) return;

        subtasks.put(subtask.getId(), subtask);
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(epic);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
            updateEpicTimes(epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask == null || !subtasks.containsKey(subtask.getId())) return;
        if (isTaskOverlapping(subtask)) {
            throw new ManagerSaveException("Подзадача пересекается по времени с существующей");
        }

        Subtask oldSubtask = subtasks.get(subtask.getId());
        prioritizedTasks.remove(oldSubtask);

        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic);

        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
            updateEpicTimes(epic);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            prioritizedTasks.remove(subtask);
            historyManager.remove(id);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic);
                updateEpicTimes(epic);
            }
        }
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.values().forEach(subtask -> {
            prioritizedTasks.remove(subtask);
            historyManager.remove(subtask.getId());
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(subtask.getId());
            }
        });
        subtasks.clear();

        epics.values().forEach(epic -> {
            epic.clearSubtaskIds();
            updateEpicStatus(epic);
            updateEpicTimes(epic);
        });
    }

    // эпики
    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return Collections.emptyList();

        List<Subtask> result = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                result.add(subtask);
            }
        }
        return result;
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic == null) return;
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
        updateEpicTimes(epic);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            historyManager.remove(id);
            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
                prioritizedTasks.removeIf(task -> task.getId() == subtaskId);
                historyManager.remove(subtaskId);
            }
        }
    }

    @Override
    public void deleteAllEpics() {
        epics.keySet().forEach(historyManager::remove);
        subtasks.keySet().forEach(historyManager::remove);
        epics.clear();
        subtasks.clear();
        prioritizedTasks.removeIf(task -> task instanceof Subtask);
    }

    // общие методы
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    // вспомогательные методы
    private void updateEpicStatus(Epic epic) {
        if (epic == null) return;

        List<Subtask> epicSubtasks = getEpicSubtasks(epic.getId());
        if (epicSubtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allDone = true;
        boolean allNew = true;

        for (Subtask subtask : epicSubtasks) {
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
        }

        if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (allNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private void updateEpicTimes(Epic epic) {
        if (epic == null) return;

        List<Subtask> subtasksList = getEpicSubtasks(epic.getId());
        if (subtasksList.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(null);
            return;
        }

        LocalDateTime start = subtasksList.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime end = subtasksList.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        epic.setStartTime(start);
        if (start != null && end != null) {
            epic.setDuration(Duration.between(start, end));
        } else {
            epic.setDuration(null);
        }
    }

    private boolean isTaskOverlapping(Task newTask) {
        if (newTask.getStartTime() == null || newTask.getDuration() == null) {
            return false;
        }

        return prioritizedTasks.stream()
                .filter(task -> task.getId() != newTask.getId())
                .filter(task -> task.getStartTime() != null && task.getDuration() != null)
                .anyMatch(existingTask -> isTimeOverlapping(newTask, existingTask));
    }

    private boolean isTimeOverlapping(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        return !end1.isBefore(start2) && !end2.isBefore(start1);
    }
}