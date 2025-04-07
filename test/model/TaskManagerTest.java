import main.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private InMemoryTaskManager manager;
    private Task task;
    private Epic epic;
    private Subtask subtask1;
    private Subtask subtask2;

    @BeforeEach
    void setUp() {
        manager = new InMemoryTaskManager();

        task = new Task("Task", "Description", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2023, 1, 1, 10, 0));

        epic = new Epic("Epic", "Epic Description");

        subtask1 = new Subtask("Subtask 1", "Description 1", Status.NEW, epic.getId(),
                Duration.ofMinutes(45), LocalDateTime.of(2023, 1, 1, 11, 0));

        subtask2 = new Subtask("Subtask 2", "Description 2", Status.IN_PROGRESS, epic.getId(),
                Duration.ofMinutes(60), LocalDateTime.of(2023, 1, 1, 12, 0));

        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
    }

    @Test
    void testGetEndTime() {
        assertEquals(LocalDateTime.of(2023, 1, 1, 10, 30), task.getEndTime());
        assertEquals(LocalDateTime.of(2023, 1, 1, 13, 0), epic.getEndTime());
    }

    @Test
    void testGetPrioritizedTasks() {
        Set<Task> prioritized = manager.getPrioritizedTasks();
        assertEquals(3, prioritized.size());
        assertTrue(prioritized.contains(task));
        assertTrue(prioritized.contains(subtask1));
        assertTrue(prioritized.contains(subtask2));
    }

    @Test
    void testTaskWithoutTimeNotInPrioritized() {
        Task noTimeTask = new Task("No time", "No time desc", Status.NEW);
        manager.addTask(noTimeTask);
        assertFalse(manager.getPrioritizedTasks().contains(noTimeTask));
    }

    @Test
    void testEpicTimeCalculation() {
        assertEquals(LocalDateTime.of(2023, 1, 1, 11, 0), epic.getStartTime());
        assertEquals(Duration.ofMinutes(105), epic.getDuration());
        assertEquals(LocalDateTime.of(2023, 1, 1, 13, 0), epic.getEndTime());
    }

    @Test
    void testTaskOverlapping() {
        Task overlappingTask = new Task("Overlapping", "Desc", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2023, 1, 1, 10, 15));
        assertTrue(manager.isTaskOverlapping(overlappingTask));

        Task nonOverlappingTask = new Task("Non-overlapping", "Desc", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2023, 1, 1, 14, 0));
        assertFalse(manager.isTaskOverlapping(nonOverlappingTask));
    }

    @Test
    void testAddOverlappingTaskThrowsException() {
        Task overlappingTask = new Task("Overlapping", "Desc", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.of(2023, 1, 1, 10, 15));

        assertThrows(ManagerSaveException.class, () -> manager.addTask(overlappingTask));
    }
}