package main;

import model.*;
import java.util.List;

public interface TaskManager {
    // Методы для задач
    List<Task> getAllTasks();
    Task getTask(int id);
    void addTask(Task task);
    void updateTask(Task task);
    void deleteTask(int id);
    void deleteAllTasks();

    // Методы для подзадач
    List<Subtask> getAllSubtasks();
    Subtask getSubtask(int id);
    void addSubtask(Subtask subtask);
    void updateSubtask(Subtask subtask);
    void deleteSubtask(int id);
    void deleteAllSubtasks();
    List<Subtask> getEpicSubtasks(int epicId);

    // Методы для эпиков
    List<Epic> getAllEpics();
    Epic getEpic(int id);
    void addEpic(Epic epic);
    void updateEpic(Epic epic);
    void deleteEpic(int id);
    void deleteAllEpics();

    // Общие методы
    List<Task> getHistory();
    List<Task> getPrioritizedTasks();
}