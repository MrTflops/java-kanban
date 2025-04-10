package service;

import main.FileBackedTaskManager;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File file;

    @Override
    @BeforeEach
    void setUp() throws IOException {
        file = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(file);
    }

    @Test
    void testDeleteTask() {
        Task task = new Task("Test", "Description", Status.NEW);
        manager.addTask(task);
        manager.deleteTask(task.getId());

        assertNull(manager.getTask(task.getId()));
    }
}