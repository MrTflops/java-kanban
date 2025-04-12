package main;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final List<Task> tasks = new ArrayList<>();
    private final List<Subtask> subtasks = new ArrayList<>();
    private final List<Epic> epics = new ArrayList<>();
    private int currentId = 1;

    @Override
    public int generateUniqueId() {
        return currentId++;
    }

    @Override
    public List<Task> getAllTasks() {
        return tasks;
    }

    @Override
    public Task getTask(int id) {
        return tasks.stream().filter(task -> task.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void addTask(Task task) {
        task.setId(generateUniqueId());
        tasks.add(task);
    }

    @Override
    public void updateTask(Task task) {
        Task existingTask = getTask(task.getId());
        if (existingTask != null) {
            existingTask.setTitle(task.getTitle());
            existingTask.setDescription(task.getDescription());
            existingTask.setStatus(task.getStatus());
            existingTask.setStartTime(task.getStartTime());
            existingTask.setDuration(task.getDuration());
        }
    }

    @Override
    public void deleteTask(int id) {
        tasks.removeIf(task -> task.getId() == id);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    // Методы для подзадач
    @Override
    public List<Subtask> getAllSubtasks() {
        return subtasks;
    }

    @Override
    public Subtask getSubtask(int id) {
        return subtasks.stream().filter(subtask -> subtask.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(generateUniqueId());
        subtasks.add(subtask);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Subtask existingSubtask = getSubtask(subtask.getId());
        if (existingSubtask != null) {
            existingSubtask.setTitle(subtask.getTitle());
            existingSubtask.setDescription(subtask.getDescription());
            existingSubtask.setStatus(subtask.getStatus());
            existingSubtask.setStartTime(subtask.getStartTime());
            existingSubtask.setDuration(subtask.getDuration());
        }
    }

    @Override
    public void deleteSubtask(int id) {
        subtasks.removeIf(subtask -> subtask.getId() == id);
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    @Override
    public List<Subtask> getSubtaskListByEpicId(int epicId) {
        List<Subtask> result = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            if (subtask.getEpicId() == epicId) {
                result.add(subtask);
            }
        }
        return result;
    }

    // Методы для эпиков
    @Override
    public List<Epic> getEpicList() {
        return epics;
    }

    @Override
    public List<Epic> getAllEpics() {
        return epics;
    }

    @Override
    public Epic getEpic(int id) {
        return epics.stream().filter(epic -> epic.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(generateUniqueId());
        epics.add(epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic existingEpic = getEpic(epic.getId());
        if (existingEpic != null) {
            existingEpic.setTitle(epic.getTitle());
            existingEpic.setDescription(epic.getDescription());
            existingEpic.setStatus(epic.getStatus());
            existingEpic.setStartTime(epic.getStartTime());
            existingEpic.setDuration(epic.getDuration());
        }
    }

    @Override
    public void deleteEpic(int id) {
        epics.removeIf(epic -> epic.getId() == id);
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(); // Возвращаем историю задач
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(); // Возвращаем приоритетные задачи
    }
}
