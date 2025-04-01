package service;

import main.InMemoryTaskManager;
import model.Task;
import model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void testAddTask() {
        Task task = new Task("Task 1", "Description", Status.NEW);
        taskManager.addTask(task);

        assertEquals(1, taskManager.getAllTasks().size());
        assertEquals(task, taskManager.getAllTasks().get(0));
    }

    @Test
    public void testDeleteTaskById() {
        Task task = new Task("Task 1", "Description", Status.NEW);
        taskManager.addTask(task);
        taskManager.deleteTaskById(task.getId());

        assertTrue(taskManager.getAllTasks().isEmpty());
    }
}
