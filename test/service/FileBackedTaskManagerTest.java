package test.service;

import main.FileBackedTaskManager;
import main.ManagerSaveException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
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
    @Override
    public void setUp() {
        file = new File("test_tasks.csv");
        manager = new FileBackedTaskManager(file);  // Инициализация менеджера с файлом
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
