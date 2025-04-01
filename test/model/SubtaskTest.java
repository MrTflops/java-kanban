import model.Subtask;
import model.Status;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubtaskTest {

    @Test
    void shouldGenerateUniqueIdsForSubtasks() {
        Subtask subtask1 = new Subtask("Subtask 1", "Desc 1", Status.NEW, 1);
        Subtask subtask2 = new Subtask("Subtask 2", "Desc 2", Status.IN_PROGRESS, 2);

        // Проверяем, что ID разные для разных подзадач
        assertNotEquals(subtask1.getId(), subtask2.getId());
    }

    @Test
    void shouldReturnCorrectEpicId() {
        Subtask subtask = new Subtask("Subtask", "Description", Status.NEW, 10);

        // Проверяем правильность Epic ID
        assertEquals(10, subtask.getEpicId());
    }

    @Test
    void shouldReturnCorrectStatus() {
        Subtask subtask = new Subtask("Subtask", "Description", Status.NEW, 10);

        // Проверяем статус подзадачи
        assertEquals(Status.NEW, subtask.getStatus());
    }
}
