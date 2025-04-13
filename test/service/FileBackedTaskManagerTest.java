package test.service;

import main.FileBackedTaskManager;
import main.ManagerSaveException;
import model.Status;
import model.Task;
import model.TaskManagerTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File file;

    @Override
    protected void initManager() {
        try {
            file = Files.createTempFile("test", ".csv").toFile();
            manager = new FileBackedTaskManager(file);
        } catch (IOException e) {
            fail("Ошибка при создании временного файла", e);
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