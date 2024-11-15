package main;

import model.Task;
import model.Epic;
import model.Subtask;

import java.util.List;

public interface TaskManager {
    Task getTask(int id);        // Получить задачу по ID
    Epic getEpic(int id);        // Получить эпик по ID
    Subtask getSubtask(int id);  // Получить подзадачу по ID
    List<Task> getHistory();     // Получить историю просмотров

    void addTask(Task task);           // Добавить задачу
    void addEpic(Epic epic);           // Добавить эпик
    void addSubtask(Subtask subtask);  // Добавить подзадачу

    void deleteTaskById(int id);       // Удалить задачу по ID
    List<Task> getAllTasks();          // Получить все задачи
    void deleteAllTasks();             // Удалить все задачи
}
