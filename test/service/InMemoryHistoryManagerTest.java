package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static model.TaskStatus.NEW;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    TaskManager taskManager;
    Task task;
    Epic epic;
    Subtask subtask;
    int taskId;
    int epicId;
    int subtaskId;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        task = new Task("Test task", "Test task description", NEW);
        taskId = taskManager.addNewTask(task);
        epic = new Epic("Test epic", "Test epic description");
        epicId = taskManager.addNewEpic(epic);
        subtask = new Subtask("Test task", "Test task description", NEW, epicId);
        subtaskId = taskManager.addNewSubtask(subtask);
    }

    @Test
    void add() {
        taskManager.getTask(taskId);
        List<Task> testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(1, testList.size(), "Неверное количество задач.");

        taskManager.getEpic(epicId);
        testList = taskManager.getHistory();
        assertEquals(2, testList.size(), "Неверное количество задач.");
        assertEquals(testList.get(0), task, "Неправильный порядок");
        assertEquals(testList.get(1), epic, "Неправильный порядок");

        taskManager.getSubtask(subtaskId);
        testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(3, testList.size(), "Неверное количество задач.");
        assertEquals(testList.get(0), task, "Неправильный порядок");
        assertEquals(testList.get(1), epic, "Неправильный порядок");
        assertEquals(testList.get(2), subtask, "Неправильный порядок");


    }

    @Test
    void deleteEpic() {
        taskManager.getTask(taskId);
        taskManager.getEpic(epicId);
        taskManager.getSubtask(subtaskId);
        taskManager.deleteEpicById(epicId);
        List<Task> testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(1, testList.size(), "Неверное количество задач.");
    }

    @Test
    void deleteTask() {
        taskManager.getTask(taskId);
        taskManager.getEpic(epicId);
        taskManager.getSubtask(subtaskId);
        taskManager.deleteTaskById(taskId);
        List<Task> testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(2, testList.size(), "Неверное количество задач.");
        assertEquals(testList.get(0), epic, "Неправильный порядок");
        assertEquals(testList.get(1), subtask, "Неправильный порядок");
    }

    @Test
    void deleteSubtask() {
        taskManager.getTask(taskId);
        taskManager.getEpic(epicId);
        taskManager.getSubtask(subtaskId);
        taskManager.deleteSubtaskById(subtaskId);
        List<Task> testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(2, testList.size(), "Неверное количество задач.");
        assertEquals(testList.get(0), task, "Неправильный порядок");
        assertEquals(testList.get(1), epic, "Неправильный порядок");
    }

    @Test
    void getHistory() {
        taskManager.getTask(taskId);
        taskManager.getEpic(epicId);
        taskManager.getSubtask(subtaskId);
        List<Task> testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(3, testList.size(), "Неверное количество задач.");
        assertEquals(testList.get(0), task, "Неправильный порядок");
        assertEquals(testList.get(1), epic, "Неправильный порядок");
        assertEquals(testList.get(2), subtask, "Неправильный порядок");

        taskManager.getTask(taskId);
        testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(3, testList.size(), "Неверное количество задач.");
        assertEquals(testList.get(0), epic, "Неправильный порядок");
        assertEquals(testList.get(1), subtask, "Неправильный порядок");
        assertEquals(testList.get(2), task, "Неправильный порядок");

    }

    @Test
    void deleteAllTask() {
        Task task1 = new Task("Test task1", "Test task1 description", NEW);
        int taskId1 = taskManager.addNewTask(task1);
        Task task2 = new Task("Test task2", "Test task2 description", NEW);
        int taskId2 = taskManager.addNewTask(task2);
        Task task3 = new Task("Test task3", "Test task3 description", NEW);
        int taskId3 = taskManager.addNewTask(task3);
        taskManager.getTask(taskId);
        taskManager.getTask(taskId1);
        taskManager.getTask(taskId2);
        taskManager.getTask(taskId3);
        taskManager.getEpic(epicId);
        taskManager.getSubtask(subtaskId);
        taskManager.clearTask();
        List<Task> testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(2, testList.size(), "Неверное количество задач.");
        assertEquals(testList.get(0), epic, "Неправильный порядок");
        assertEquals(testList.get(1), subtask, "Неправильный порядок");
    }

    @Test
    void deleteAllSubtask() {
        Subtask subtask1 = new Subtask("Test task1", "Test task1 description", NEW, epicId);
        int subtaskId1 = taskManager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test task2", "Test task2 description", NEW, epicId);
        int subtaskId2 = taskManager.addNewSubtask(subtask2);
        Subtask subtask3 = new Subtask("Test task3", "Test task3 description", NEW, epicId);
        int subtaskId3 = taskManager.addNewSubtask(subtask3);
        taskManager.getTask(taskId);
        taskManager.getEpic(epicId);
        taskManager.getSubtask(subtaskId);
        taskManager.getSubtask(subtaskId1);
        taskManager.getSubtask(subtaskId2);
        taskManager.getSubtask(subtaskId3);
        taskManager.clearSubtask();
        List<Task> testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(2, testList.size(), "Неверное количество задач.");
        assertEquals(testList.get(0), task, "Неправильный порядок");
        assertEquals(testList.get(1), epic, "Неправильный порядок");
    }

    @Test
    void deleteAllEpic() {
        Epic epic1 = new Epic("Test epic1", "Test epic1 description");
        int epicId1 = taskManager.addNewEpic(epic1);
        Epic epic2 = new Epic("Test epic2", "Test epic2 description");
        int epicId2 = taskManager.addNewEpic(epic2);
        Epic epic3 = new Epic("Test epic3", "Test epic3 description");
        int epicId3 = taskManager.addNewEpic(epic3);
        taskManager.getTask(taskId);
        taskManager.getEpic(epicId);
        taskManager.getEpic(epicId1);
        taskManager.getEpic(epicId2);
        taskManager.getEpic(epicId3);
        taskManager.getSubtask(subtaskId);
        taskManager.clearEpic();
        List<Task> testList = taskManager.getHistory();
        assertNotNull(testList, "Задачи не возвращаются.");
        assertEquals(1, testList.size(), "Неверное количество задач.");
        assertEquals(testList.getFirst(), task, "Неправильный порядок");
    }
}