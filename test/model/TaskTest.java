import model.Task;
import model.Epic;
import model.Subtask;
import model.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTasksEqualityById() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);

        task2.setId(task1.getId()); // Принудительно устанавливаем одинаковый ID

        assertEquals(task1, task2);
    }

    @Test
    void testSubtasksEqualityById() {
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", Status.NEW, 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", Status.NEW, 1);

        subtask2.setId(subtask1.getId()); // Принудительно устанавливаем одинаковый ID

        assertEquals(subtask1, subtask2);
    }
}
