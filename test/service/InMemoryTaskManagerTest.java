package test.service;

import main.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private InMemoryTaskManager manager;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();
        task = new Task("Test Task", "Test Description", Status.NEW);
        epic = new Epic("Test Epic", "Test Epic Description");
        subtask = new Subtask("Test Subtask", "Test Subtask Description", Status.NEW, 1);

        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
    }

    @Test
    void getTask_shouldReturnTaskAndAddToHistory() {
        Task result = manager.getTask(task.getId());

        assertNotNull(result);
        assertEquals(task, result);
        assertTrue(manager.getHistory().contains(task));
    }

    @Test
    void getTask_withAddToHistoryFalse_shouldNotAddToHistory() {
        Task result = manager.getTask(task.getId(), false);

        assertNotNull(result);
        assertEquals(task, result);
        assertFalse(manager.getHistory().contains(task));
    }

    @Test
    void getEpic_shouldReturnEpicAndAddToHistory() {
        Epic result = manager.getEpic(epic.getId());

        assertNotNull(result);
        assertEquals(epic, result);
        assertTrue(manager.getHistory().contains(epic));
    }

    @Test
    void getSubtask_shouldReturnSubtaskAndAddToHistory() {
        Subtask result = manager.getSubtask(subtask.getId());

        assertNotNull(result);
        assertEquals(subtask, result);
        assertTrue(manager.getHistory().contains(subtask));
    }

    @Test
    void tasksWithDifferentIds_shouldNotConflict() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);
        task2.setId(100); // Устанавливаем кастомный ID

        manager.addTask(task1);
        manager.addTask(task2);

        assertEquals(task1, manager.getTask(task1.getId()));
        assertEquals(task2, manager.getTask(task2.getId()));
        assertNotEquals(manager.getTask(task1.getId()), manager.getTask(task2.getId()));
    }

    @Test
    void addedTask_shouldKeepImmutability() {
        Task originalTask = new Task("Original", "Original Desc", Status.NEW);
        manager.addTask(originalTask);

        Task retrievedTask = manager.getTask(originalTask.getId());

        assertEquals(originalTask.getTitle(), retrievedTask.getTitle());
        assertEquals(originalTask.getDescription(), retrievedTask.getDescription());
        assertEquals(originalTask.getStatus(), retrievedTask.getStatus());
        assertEquals(originalTask.getId(), retrievedTask.getId());
    }
}