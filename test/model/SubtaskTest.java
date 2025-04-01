package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import static model.TaskStatus.*;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    TaskManager taskManager = Managers.getDefault();
    Epic epic1;
    int epicId;
    Subtask subtask1;
    int subtask1Id;
    Subtask savedSubtask1;

    @BeforeEach
    void setUp(){
        epic1 = new Epic("Test epic1", "Test epic1 description");
        epicId = taskManager.addNewEpic(epic1);
        subtask1 = new Subtask("Test subtask1", "Test subtask1 description", NEW , epicId);
        subtask1Id = taskManager.addNewSubtask(subtask1);
        savedSubtask1 = taskManager.getSubtask(subtask1Id);
    }

    @Test
    void subtaskComparisonsById() {
        subtask1.setNameTask("Test subtask2");
        subtask1.setDescriptionTask("Test subtask2 description");
        subtask1.setStatusOfTask(IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        Subtask savedSubtask2 = taskManager.getSubtask(subtask1Id);

        assertEquals(savedSubtask2, savedSubtask1, "Задачи не совпадают.");
    }

    @Test
    void checkSubtaskCannotAddItselfAsSubtask() {
        Task subtask2 = new Subtask("Test subtask1", "Test subtask1 description", NEW , subtask1Id);
        int subtask2Id = taskManager.addNewSubtask((Subtask) subtask2);
        assertEquals(0,subtask2Id,"Ошибка, субтаск добавлен!");
    }
}