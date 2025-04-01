import main.InMemoryHistoryManager;
import model.Task;
import model.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    @Test
    void testTaskHistoryPreservesPreviousVersion() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task = new Task("Task", "Description", Status.NEW);

        historyManager.add(task);

        Task updatedTask = new Task("Task Updated", "Updated Description", Status.DONE);
        updatedTask.setId(task.getId());

        historyManager.add(updatedTask);

        assertNotEquals(historyManager.getHistory().get(0), updatedTask);
    }
}