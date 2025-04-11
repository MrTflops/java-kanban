package test.service;

import main.FileBackedTaskManager;
import main.ManagerSaveException;
import main.TaskManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File file;

    @BeforeEach
    void setUp() throws IOException {
        file = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(file);
    }

    @Test
    void testAddAndGetTask() {
        Task task = new Task("Test Task", "Description", Status.NEW);
        manager.addTask(task);

        Task retrievedTask = manager.getTask(task.getId());
        assertEquals(task, retrievedTask);
    }

    @Test
    void testAddTaskWithOverlappingTime() {
        Task task1 = new Task("Task 1", "Description", Status.NEW);
        task1.setStartTime("2023-01-01T10:00:00");
        task1.setDuration("00:30");

        Task task2 = new Task("Task 2", "Description", Status.NEW);
        task2.setStartTime("2023-01-01T10:15:00");
        task2.setDuration("00:30");

        manager.addTask(task1);

        assertThrows(ManagerSaveException.class, () -> {
            manager.addTask(task2);
        });
    }

    @Test
    void testGetAllTasks() {
        Task task1 = new Task("Task 1", "Description", Status.NEW);
        Task task2 = new Task("Task 2", "Description", Status.NEW);

        manager.addTask(task1);
        manager.addTask(task2);

        List<Task> tasks = manager.getAllTasks();
        assertEquals(2, tasks.size());
    }
}
