package test.service;

import main.FileBackedTaskManager;
import main.TaskManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private File file;

    @BeforeEach
    public void setUp() throws IOException {
        // Обработка исключения IOException, которое может возникнуть при создании временного файла
        file = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(file);
    }

    @Test
    void testAddTask() {
        Task task = new Task("Test Task", "Description", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0), Duration.ofMinutes(30));

        manager.addTask(task);

        Task retrievedTask = manager.getTask(task.getId());
        assertEquals(task, retrievedTask);
    }

    @Test
    void testGetAllTasks() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0), Duration.ofMinutes(30));
        Task task2 = new Task("Task 2", "Description 2", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 11, 0), Duration.ofMinutes(45));

        manager.addTask(task1);
        manager.addTask(task2);

        List<Task> tasks = manager.getAllTasks();
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void testPrioritizedTasks() {
        Task task1 = new Task("Task 1", "Description", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0), Duration.ofMinutes(30));
        Task task2 = new Task("Task 2", "Description", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 9, 0), Duration.ofMinutes(45));

        manager.addTask(task1);
        manager.addTask(task2);

        List<Task> prioritized = manager.getPrioritizedTasks();
        assertEquals(2, prioritized.size());
        assertEquals("Task 2", prioritized.get(0).getTitle());
    }

    @Test
    void testTaskOverlapping() {
        Task task = new Task("Task", "Description", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0), Duration.ofMinutes(30));
        manager.addTask(task);

        Task overlappingTask = new Task("Overlapping", "Description", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 15), Duration.ofMinutes(30));

        assertThrows(ManagerSaveException.class, () -> manager.addTask(overlappingTask));
    }
}
