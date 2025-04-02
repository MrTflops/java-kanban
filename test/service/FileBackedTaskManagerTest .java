package test.service;

import main.FileBackedTaskManager;
import model.*;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("tasks", ".csv").toFile();
        manager = new FileBackedTaskManager(tempFile);
    }

    @Test
    void testSaveAndLoadEmptyFile() {
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loadedManager.getAllTasks().isEmpty());
    }

    @Test
    void testSaveAndLoadTasks() {
        Task task = new Task("Task 1", "Description 1", Status.NEW);
        Epic epic = new Epic("Epic 1", "Description Epic");
        Subtask subtask = new Subtask("Subtask 1", "Description Subtask", Status.NEW, 2);

        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertEquals(3, loadedManager.getAllTasks().size());
        assertEquals(task.getTitle(), loadedManager.getTask(task.getId()).getTitle());
        assertEquals(epic.getTitle(), loadedManager.getEpic(epic.getId()).getTitle());
        assertEquals(subtask.getTitle(), loadedManager.getSubtask(subtask.getId()).getTitle());
    }

    @Test
    void testSaveAfterDeletion() {
        Task task = new Task("Task to delete", "Description", Status.NEW);
        manager.addTask(task);

        manager.deleteTaskById(task.getId());

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertNull(loadedManager.getTask(task.getId()));
    }
}