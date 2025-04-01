package test.service;

import main.InMemoryHistoryManager;
import model.Task;
import model.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;

    @Test
    void add_shouldAddTaskToHistory() {
        historyManager = new InMemoryHistoryManager();
        Task task = new Task("Test", "Description", Status.NEW);

        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task, historyManager.getHistory().get(0));
    }

    @Test
    void add_duplicateTask_shouldMoveToEnd() {
        historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Desc 1", Status.NEW);
        Task task2 = new Task("Task 2", "Desc 2", Status.IN_PROGRESS);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);

        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task2, historyManager.getHistory().get(0));
        assertEquals(task1, historyManager.getHistory().get(1));
    }

    @Test
    void remove_shouldDeleteTaskFromHistory() {
        historyManager = new InMemoryHistoryManager();
        Task task = new Task("To remove", "Desc", Status.DONE);
        task.setId(1);

        historyManager.add(task);
        historyManager.remove(1);

        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void getHistory_shouldReturnCorrectOrder() {
        historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("First", "1", Status.NEW);
        Task task2 = new Task("Second", "2", Status.NEW);
        Task task3 = new Task("Third", "3", Status.NEW);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        assertEquals(3, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().get(0));
        assertEquals(task2, historyManager.getHistory().get(1));
        assertEquals(task3, historyManager.getHistory().get(2));
    }

    @Test
    void taskHistory_shouldUpdateTaskVersion() {
        historyManager = new InMemoryHistoryManager();
        Task task = new Task("Task", "Description", Status.NEW);
        task.setId(1);

        historyManager.add(task);

        Task updatedTask = new Task("Updated", "New Desc", Status.DONE);
        updatedTask.setId(1); // Тот же ID

        historyManager.add(updatedTask);

        assertEquals(1, historyManager.getHistory().size());
        assertEquals("Updated", historyManager.getHistory().get(0).getTitle()); // Проверяем обновлённые поля
        assertEquals(Status.DONE, historyManager.getHistory().get(0).getStatus());
    }
}