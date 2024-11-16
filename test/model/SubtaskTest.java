import model.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void testSubtaskCannotBeItsOwnEpic() {
        Subtask subtask = new Subtask("Subtask", "Description", Status.NEW, 1);

        assertNotEquals(subtask.getId(), subtask.getEpicId());
    }
}
