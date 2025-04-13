import model.Task;
import model.Epic;
import model.Subtask;
import model.Status;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    void generateUniqueIdsForTasks() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.IN_PROGRESS);

        // Проверяем, что ID разные для разных задач
        assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    void returnCorrectTitleAndDescription() {
        Task task = new Task("Test Task", "Test Description", Status.DONE);

        // Проверяем, что данные корректные
        assertEquals("Test Task", task.getTitle());
        assertEquals("Test Description", task.getDescription());
        assertEquals(Status.DONE, task.getStatus());
    }

    @Test
    void returnCorrectStatus() {
        Task task = new Task("Test Task", "Test Description", Status.NEW);

        // Проверяем статус
        assertEquals(Status.NEW, task.getStatus());
    }
}