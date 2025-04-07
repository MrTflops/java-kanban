package test;

import main.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.Status;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;

    protected abstract T createManager();

    @BeforeEach
    void setUp() {
        manager = createManager();
    }

    @Test
    abstract void testAddAndGetTask();

    @Test
    abstract void testAddAndGetEpic();

    @Test
    abstract void testAddAndGetSubtask();

    @Test
    abstract void testDeleteTask();

    @Test
    abstract void testDeleteEpic();

    @Test
    abstract void testDeleteSubtask();

    @Test
    abstract void testGetHistory();

    @Test
    void testGetPrioritizedTasks() {
        Task task1 = new Task("Task 1", "Desc 1", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2023, 1, 1, 10, 0));
        Task task2 = new Task("Task 2", "Desc 2", Status.NEW,
                Duration.ofMinutes(45), LocalDateTime.of(2023, 1, 1, 9, 0));

        manager.addTask(task1);
        manager.addTask(task2);

        Set<Task> prioritized = manager.getPrioritizedTasks();
        assertEquals(2, prioritized.size());
        assertEquals(task2, prioritized.iterator().next()); // task2 должен быть первым, так как начинается раньше
    }

    @Test
    void testTaskOverlapping() {
        Task task1 = new Task("Task 1", "Desc 1", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2023, 1, 1, 10, 0));
        manager.addTask(task1);

        Task overlappingTask = new Task("Overlapping", "Desc", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2023, 1, 1, 10, 15));
        assertTrue(manager.isTaskOverlapping(overlappingTask));
    }

    @Test
    void testEpicStatusCalculation() {
        Epic epic = new Epic("Epic", "Description");
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Sub1", "Desc1", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Sub2", "Desc2", Status.DONE, epic.getId());

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }
}