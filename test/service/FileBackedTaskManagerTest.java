package test.service;

import main.FileBackedTaskManager;
import main.ManagerSaveException;
import model.Status;
import model.Task;
import model.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File file;

    @BeforeEach
    public void setUp() {
        try {
            file = File.createTempFile("test_tasks", ".csv");
            manager = new FileBackedTaskManager(file);
        } catch (Exception e) {
            fail("Failed to create temp file", e);
        }
    }

    @AfterEach
    void tearDown() {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    @Test
    void testAddAndGetTask() {
        Task task = new Task("Task 1", "Description", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0), Duration.ofMinutes(30));
        manager.addTask(task);

        Task retrievedTask = manager.getTask(task.getId());
        assertNotNull(retrievedTask);
        assertEquals(task.getTitle(), retrievedTask.getTitle());
    }
}