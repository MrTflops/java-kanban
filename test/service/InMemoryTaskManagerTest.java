package service;

import main.InMemoryTaskManager;
import model.Task;
import model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void testAddTask() {
        Task task = new Task("Task 1", "Description", Status.NEW);
        taskManager.addTask(task);

        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(task, taskManager.getAllTasks().get(0));
    }

    @Test
    void testDeleteTaskById() {
        Task task = new Task("Task 1", "Description", Status.NEW);
        taskManager.addTask(task);
        taskManager.deleteTaskById(task.getId());

        assertTrue(taskManager.getAllTasks().isEmpty());
    }
}
