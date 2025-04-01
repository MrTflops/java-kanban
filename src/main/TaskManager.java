package main;

import model.Task;
import model.Epic;
import model.Subtask;

import java.util.List;

public interface TaskManager {
    // Основной метод с контролем истории
    Task getTask(int id, boolean addToHistory);

    // Перегруженный метод для удобства (добавляет в историю по умолчанию)
    default Task getTask(int id) {

        return getTask(id, true);
    }

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    List<Task> getHistory();

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void deleteTaskById(int id);

    List<Task> getAllTasks();

    void deleteAllTasks();
}