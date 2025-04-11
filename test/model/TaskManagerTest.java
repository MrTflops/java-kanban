package model;

import main.FileBackedTaskManager;
import main.ManagerSaveException;
import main.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private static final File TEST_FILE = new File("test_tasks.csv");

    @Override
    @BeforeEach
    protected void setUp() {
        // Удаляем файл перед каждым тестом, чтобы избежать проблем с остаточными данными
        if (TEST_FILE.exists()) {
            TEST_FILE.delete();
        }
        manager = new FileBackedTaskManager(TEST_FILE); // Инициализация менеджера с файлом
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

    @Test
    void testLoadFromFile() {
        // Сохраняем несколько задач
        Task task1 = new Task("Task 1", "Description", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 10, 0), Duration.ofMinutes(30));
        Task task2 = new Task("Task 2", "Description", Status.NEW,
                LocalDateTime.of(2023, 1, 1, 9, 0), Duration.ofMinutes(45));

        manager.addTask(task1);
        manager.addTask(task2);

        // Загружаем задачи из файла в новый менеджер
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(TEST_FILE);
        List<Task> loadedTasks = loadedManager.getAllTasks();

        assertEquals(2, loadedTasks.size());
        assertEquals("Task 1", loadedTasks.get(0).getTitle());
        assertEquals("Task 2", loadedTasks.get(1).getTitle());
    }

    // Очистка после всех тестов
    @Override
    protected void tearDown() {
        if (TEST_FILE.exists()) {
            TEST_FILE.delete();  // Удаляем тестовый файл после выполнения тестов
        }
    }
}
