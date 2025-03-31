package main;

import model.Task;
import model.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    @Test
    void testAdd() {
        HistoryManager manager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);

        manager.add(task1);
        manager.add(task2);

        assertEquals(2, manager.getHistory().size());
        assertEquals(task1, manager.getHistory().get(0));
        assertEquals(task2, manager.getHistory().get(1));
    }

    @Test
    void testRemove() {
        HistoryManager manager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);

        manager.add(task1);
        manager.add(task2);
        manager.remove(task1.getId());

        assertEquals(1, manager.getHistory().size());
        assertEquals(task2, manager.getHistory().get(0));
    }

    @Test
    void testDuplicateTasks() {
        HistoryManager manager = new InMemoryHistoryManager();
        Task task = new Task("Task", "Description", Status.NEW);

        manager.add(task);
        manager.add(task);

        assertEquals(1, manager.getHistory().size());
    }

    @Test
    void testEmptyHistory() {
        HistoryManager manager = new InMemoryHistoryManager();
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    void testRemoveFromMiddle() {
        HistoryManager manager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        Task task3 = new Task("Task 3", "Description 3", Status.NEW);

        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.remove(task2.getId());

        assertEquals(2, manager.getHistory().size());
        assertEquals(task1, manager.getHistory().get(0));
        assertEquals(task3, manager.getHistory().get(1));
    }
}