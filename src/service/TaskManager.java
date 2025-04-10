package service;

import model.Task;
import model.Epic;
import model.Subtask;

import java.util.List;

public interface TaskManager {
    // Основной метод
    Task getTask(int id);

    // Версия с контролем истории (если нужно)
    Task getTask(int id, boolean addToHistory);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    List<Task> getHistory();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void deleteTaskById(int id);

    List<Task> getAllTasks();

    void deleteAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    List<Task> getPrioritizedTasks();

    List<Subtask> getEpicSubtasks(int epicId);

}