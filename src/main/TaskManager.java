package main;

import model.Task;
import model.Epic;
import model.Subtask;

import java.util.List;

public interface TaskManager {

    // Получение задачи по ID
    Task getTask(int id);

    // Получение эпика по ID
    Epic getEpic(int id);

    // Получение подзадачи по ID
    Subtask getSubtask(int id);

    // Получение истории просмотров
    List<Task> getHistory();

    // Добавление задачи
    void addTask(Task task);

    // Добавление эпика
    void addEpic(Epic epic);

    // Добавление подзадачи
    void addSubtask(Subtask subtask);

    // Удаление задачи по ID
    void deleteTaskById(int id);

    // Получение всех задач
    List<Task> getAllTasks();

    // Удаление всех задач
    void deleteAllTasks();
}
