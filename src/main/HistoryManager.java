package main;

import model.Task;

import java.util.List;

public interface HistoryManager {
    /**
     * Добавление задачи в историю просмотров
     */
    void add(Task task);

    /**
     * Удаление задачи по ID
     */
    void remove(int id);

    /**
     * Получение всей истории просмотров в виде списка задач
     */
    List<Task> getHistory();
}
