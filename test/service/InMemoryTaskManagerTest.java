package service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;

import java.util.List;

import static model.TaskStatus.NEW;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager taskManager;
    Task task;
    int taskId;
    Task savedTask;
    Epic epic;
    int epicId;
    Epic savedEpic;
    Subtask subtask;
    int subtaskId;
    Subtask savedSubtask;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        task = new Task("Test task", "Test task description", NEW);
        epic = new Epic("Test epic", "Test epic description");
    }

    @Test
    void addNewTaskTest() {
        taskId = taskManager.addNewTask(task);
        savedTask = taskManager.getTask(taskId);
        assertNotNull(savedTask, "Задача не найдена."); //тест, в котором проверяется неизменность задачи
        assertEquals(task, savedTask, "Задачи не совпадают.");

    }

    @Test
    void getTaskListTest() {
        taskId = taskManager.addNewTask(task);
        savedTask = taskManager.getTask(taskId);
        List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи не возвращаются."); //добавляет задачу и может найти ее по id;
        assertEquals(1, tasks.size(), "Неверное количество задач.");
    }

    @Test
    void addNewEpicTest() {
        epicId = taskManager.addNewEpic(epic);
        savedEpic = taskManager.getEpic(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
    }

    @Test
    void getEpicListTest() {
        epicId = taskManager.addNewEpic(epic);
        savedEpic = taskManager.getEpic(epicId);
        List<Epic> epics = taskManager.getEpicList();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
    }

    @Test
    void addNewSubtaskTest() {
        epicId = taskManager.addNewEpic(epic);
        subtask = new Subtask("Test subtask", "Test subtask description", NEW, epicId);
        subtaskId = taskManager.addNewSubtask(subtask);
        savedSubtask = taskManager.getSubtask(subtaskId);
        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
    }

    @Test
    void getSubtaskListTest() {
        epicId = taskManager.addNewEpic(epic);
        subtask = new Subtask("Test subtask", "Test subtask description", NEW, epicId);
        subtaskId = taskManager.addNewSubtask(subtask);
        savedSubtask = taskManager.getSubtask(subtaskId);
        List<Subtask> subtasks = taskManager.getSubtaskList();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
    }

    @Test
    void historyNotNull() {
        taskId = taskManager.addNewTask(task);
        savedTask = taskManager.getTask(taskId);
        epicId = taskManager.addNewEpic(epic);
        savedEpic = taskManager.getEpic(epicId);
        subtask = new Subtask("Test subtask", "Test subtask description", NEW, epicId);
        subtaskId = taskManager.addNewSubtask(subtask);
        savedSubtask = taskManager.getSubtask(subtaskId);
        List<Task> history = taskManager.getHistory();
        assertNotNull(history);
    }

}