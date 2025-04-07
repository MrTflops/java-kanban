package main;

import model.Task;
import model.Epic;
import model.Subtask;

import java.util.List;

public interface TaskManager {
    List<Task> getTasks();
    Task getTask(int id);
    Task getTask(int id, boolean addToHistory);
    void addTask(Task task);
    void updateTask(Task task);
    void deleteTaskById(int id);
    void deleteAllTasks();

    List<Subtask> getSubtasks();
    Subtask getSubtask(int id);
    void addSubtask(Subtask subtask);
    void updateSubtask(Subtask subtask);
    void deleteSubtaskById(int id);
    void deleteAllSubtasks();

    List<Epic> getEpics();
    Epic getEpic(int id);
    List<Subtask> getEpicSubtasks(int epicId);
    void addEpic(Epic epic);
    void deleteEpicById(int id);
    void deleteAllEpics();

    List<Task> getHistory();
    List<Task> getPrioritizedTasks();
}