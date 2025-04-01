package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import static model.TaskStatus.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    TaskManager taskManager = Managers.getDefault();
    Task task1;
    Task savedTask;
    int taskId;

    @BeforeEach
    void setUp(){
        task1 = new Task("Test task1", "Test task1 description", NEW);
        taskId = taskManager.addNewTask(task1);
        savedTask = taskManager.getTask(taskId);
    }

    @Test
    void taskComparisonsById() {
        task1.setNameTask("Test task2");
        task1.setDescriptionTask("Test task2 description");
        task1.setStatusOfTask(IN_PROGRESS);
        taskManager.updateTask(task1);
        Task savedTask1 = taskManager.getTask(taskId);

        assertEquals(savedTask1, savedTask, "Задачи не совпадают.");
    }




}