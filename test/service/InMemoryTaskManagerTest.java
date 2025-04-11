package service;

import main.InMemoryTaskManager;
import model.TaskManagerTest;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected void initManager() {
        manager = new InMemoryTaskManager();
    }

    @BeforeEach
    public void setUp() {
        initManager();
    }

    @Test
    void testGetTask() {
        Task task = new Task("Test", "Description", Status.NEW);
        manager.addTask(task);

        Task result = manager.getTask(task.getId());
        assertEquals(task, result);
    }
}