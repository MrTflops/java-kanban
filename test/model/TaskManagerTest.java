package model;

import main.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    @BeforeEach
    abstract void setUp();

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