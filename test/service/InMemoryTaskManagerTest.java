package service;

import main.InMemoryTaskManager;
import model.TaskManagerTest;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void setUp() {
        manager = new InMemoryTaskManager();
    }

    @Test
    void testGetTask() {
        Task task = new Task("Test", "Description", Status.NEW);
        manager.addTask(task);

        Task result = manager.getTask(task.getId());
        assertEquals(task, result);
    }
}