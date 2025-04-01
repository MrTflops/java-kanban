import main.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldAddAndRetrieveTasks() {
        Task task1 = new Task("Task 1", "Desc 1", Status.NEW);
        Task task2 = new Task("Task 2", "Desc 2", Status.IN_PROGRESS);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        // Проверяем, что задачи добавлены и могут быть получены
        assertEquals(task1, taskManager.getTask(task1.getId()));
        assertEquals(task2, taskManager.getTask(task2.getId()));

        // Проверяем, что ID разные у разных задач
        assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    void shouldReturnEmptyHistoryInitially() {
        // Проверяем, что история пустая на старте
        assertTrue(taskManager.getHistory().isEmpty());
    }

    @Test
    void shouldAddTaskToHistoryWhenAdded() {
        Task task1 = new Task("Task 1", "Desc 1", Status.NEW);
        taskManager.addTask(task1);

        // Проверяем, что история не пуста
        assertFalse(taskManager.getHistory().isEmpty());
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    void shouldReturnEmptyHistoryForEmptyManager() {
        // Проверяем, что история пуста, если нет задач
        assertTrue(taskManager.getHistory().isEmpty());
    }
}
