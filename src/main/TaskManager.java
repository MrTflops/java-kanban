package main;

import model.Task;
import model.Epic;
import model.Subtask;
import java.util.List;

public interface TaskManager {

    Task getTask(int id);

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