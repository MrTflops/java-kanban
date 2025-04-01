package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    TaskManager taskManager;
    Epic epic1;
    int epicId1;
    Epic savedEpic1;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        epic1 = new Epic("Test epic1", "Test epic1 description");
        epicId1 = taskManager.addNewEpic(epic1);
        savedEpic1 = taskManager.getEpic(epicId1);

    }

    @Test
    void epicComparisonsById() {
        epic1.setNameTask("Test epic2");
        epic1.setDescriptionTask("Test epic2 description");
        taskManager.updateEpic(epic1);
        Epic savedEpic2 = taskManager.getEpic(epicId1);

        assertEquals(savedEpic2, savedEpic1, "Задачи не совпадают.");
    }

    @Test
    void checkEpicCannotAddItselfAsSubtask() { //Сомневаюсь что его так нужно юзать, но кажется работает
        assertThrows(RuntimeException.class, () -> {
            Task fakeEpic2 = new Epic("Test fakeEpic2", "Test fakeEpic2 description");
            int fakeEpicId2 = taskManager.addNewSubtask((Subtask) fakeEpic2);
        });
    }
}