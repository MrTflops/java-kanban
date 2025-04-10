package main;

import model.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.Duration;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final Set<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(
                    Task::getStartTime,
                    Comparator.nullsLast(Comparator.naturalOrder())
            ).thenComparing(Task::getId)
    );
    protected int nextId = 1;

    // Методы для задач
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtaskListByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            throw new NotFoundException("Эпик с id=" + epicId + " не найден");
        }
        return getEpicSubtasks(epicId);
    }

    @Override
    public void deleteEpicById(int id) {
        deleteEpic(id);
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void addTask(Task task) {
        if (task == null) return;
        task.setId(nextId++);
        if (isTaskOverlapping(task)) {
            throw new ManagerSaveException("Задача пересекается по времени с существующей");
        }
        tasks.put(task.getId(), task);
        addToPrioritized(task);
    }

    @Override
    public void updateTask(Task task) {
        if (task == null || !tasks.containsKey(task.getId())) return;
        if (isTaskOverlapping(task)) {
            throw new ManagerSaveException("Задача пересекается по времени с существующей");
        }
        prioritizedTasks.remove(tasks.get(task.getId()));
        tasks.put(task.getId(), task);
        addToPrioritized(task);
    }

    @Override
    public void deleteTask(int id) {
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

    // Методы для подзадач
    @Override
    public List<Subtask> getAllSubtasks() {
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
        if (subtask == null || epics.get(subtask.getEpicId()) == null) return;
        subtask.setId(nextId++);
        if (isTaskOverlapping(subtask)) {
            throw new ManagerSaveException("Подзадача пересекается по времени с существующей");
        }
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtaskId(subtask.getId());
        updateEpicStatus(epics.get(subtask.getEpicId()));
        addToPrioritized(subtask);
        updateEpicTimes(epics.get(subtask.getEpicId()));
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask == null || !subtasks.containsKey(subtask.getId())) return;
        if (isTaskOverlapping(subtask)) {
            throw new ManagerSaveException("Подзадача пересекается по времени с существующей");
        }
        prioritizedTasks.remove(subtasks.get(subtask.getId()));
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epics.get(subtask.getEpicId()));
        addToPrioritized(subtask);
        updateEpicTimes(epics.get(subtask.getEpicId()));
    }

    @Override
    public void deleteSubtask(int id) {
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

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return Collections.emptyList();
        return epic.getSubtaskIds().stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .toList();
    }

    // Методы для эпиков
    @Override
    public List<Epic> getAllEpics() {
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
    public void addEpic(Epic epic) {
        if (epic == null) return;
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
        updateEpicTimes(epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null || !epics.containsKey(epic.getId())) return;
        Epic existingEpic = epics.get(epic.getId());
        existingEpic.setTitle(epic.getTitle());
        existingEpic.setDescription(epic.getDescription());
        updateEpicStatus(existingEpic);
        updateEpicTimes(existingEpic);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            historyManager.remove(id);
            epic.getSubtaskIds().forEach(subtaskId -> {
                subtasks.remove(subtaskId);
                prioritizedTasks.removeIf(task -> task.getId() == subtaskId);
                historyManager.remove(subtaskId);
            });
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

    // Общие методы
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    // Вспомогательные методы
    private void addToPrioritized(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    private void updateEpicStatus(Epic epic) {
        if (epic == null) return;
        List<Subtask> subtasks = getEpicSubtasks(epic.getId());

        if (subtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() != Status.NEW) allNew = false;
            if (subtask.getStatus() != Status.DONE) allDone = false;
        }

        if (allNew) epic.setStatus(Status.NEW);
        else if (allDone) epic.setStatus(Status.DONE);
        else epic.setStatus(Status.IN_PROGRESS);
    }

    private void updateEpicTimes(Epic epic) {
        if (epic == null) return;
        List<Subtask> subtasks = getEpicSubtasks(epic.getId());

        if (subtasks.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(null);
            return;
        }

        LocalDateTime start = subtasks.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime end = subtasks.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        epic.setStartTime(start);
        epic.setDuration(start != null && end != null ? Duration.between(start, end) : null);
    }

    private boolean isTaskOverlapping(Task newTask) {
        if (newTask.getStartTime() == null || newTask.getDuration() == null) return false;

        return prioritizedTasks.stream()
                .filter(task -> task.getId() != newTask.getId())
                .filter(task -> task.getStartTime() != null && task.getDuration() != null)
                .anyMatch(existingTask -> isTimeOverlapping(newTask, existingTask));
    }

    private boolean isTimeOverlapping(Task task1, Task task2) {
        return !task1.getEndTime().isBefore(task2.getStartTime()) &&
                !task2.getEndTime().isBefore(task1.getStartTime());
    }
}