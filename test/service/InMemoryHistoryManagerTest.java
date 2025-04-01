package service;

import main.InMemoryHistoryManager;
import model.Task;
import model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testAddTaskToHistory() {
        Task task = new Task(1, "Test Task", "Description", Status.NEW);
        historyManager.add(task);

        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task, historyManager.getHistory().get(0));
    }

    @Test
    void testRemoveTaskFromHistory() {
        Task task = new Task(1, "Test Task", "Description", Status.NEW);
        historyManager.add(task);
        historyManager.remove(1);

        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void testGetHistoryOrder() {
        Task task1 = new Task(1, "Task 1", "Description", Status.NEW);
        Task task2 = new Task(2, "Task 2", "Description", Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);

        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().get(0));
        assertEquals(task2, historyManager.getHistory().get(1));
    }
}
