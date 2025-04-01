import main.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    @Test
    void testAddTasksAndFindById() {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task = new Task("Task", "Task description", Status.NEW);
        Epic epic = new Epic("Epic", "Epic description");
        Subtask subtask = new Subtask("Subtask", "Subtask description", Status.NEW, epic.getId());

        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask);

        assertEquals(task, manager.getTask(task.getId()));
        assertEquals(epic, manager.getEpic(epic.getId()));
        assertEquals(subtask, manager.getSubtask(subtask.getId()));
    }

    @Test
    void testTasksWithCustomAndGeneratedIdsDoNotConflict() {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.NEW);

        task2.setId(100); // Устанавливаем кастомный ID

        manager.addTask(task1);
        manager.addTask(task2);

        assertEquals(task1, manager.getTask(task1.getId()));
        assertEquals(task2, manager.getTask(task2.getId()));
    }

    @Test
    void testTaskImmutabilityWhenAdded() {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task task = new Task("Task", "Description", Status.NEW);
        manager.addTask(task);

        Task retrievedTask = manager.getTask(task.getId());

        assertEquals(task.getTitle(), retrievedTask.getTitle());
        assertEquals(task.getDescription(), retrievedTask.getDescription());
        assertEquals(task.getStatus(), retrievedTask.getStatus());
        assertEquals(task.getId(), retrievedTask.getId());
    }
}