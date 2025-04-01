package main;

import model.Task;
import model.Epic;
import model.Subtask;

import java.util.List;

public interface TaskManager {

    Task getTask(int id, boolean addToHistory);  // Новый параметр для контроля добавления в историю

    Task getTask(int id);  // Версия по умолчанию (добавляет в историю)
    
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
