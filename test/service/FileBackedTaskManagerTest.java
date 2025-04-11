package service;

import main.FileBackedTaskManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.TaskManagerTest;


import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNull;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File file;

    @BeforeEach
    public void setUp() throws IOException {
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