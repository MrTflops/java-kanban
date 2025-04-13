import model.Epic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void epicCannotAddItselfAsSubtask() {
        Epic epic = new Epic("Epic", "Description");
        epic.addSubtaskId(epic.getId()); // пробуем добавить эпик сам в себя

        assertFalse(epic.getSubtaskIds().contains(epic.getId()));
    }
}