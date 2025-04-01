import model.*;
import main.Managers;
import main.TaskManager;

public class Main {

    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();

        // Создание

        Task task1 = new Task("Task #1", "Task1 description");
        final int taskId1 = manager.addNewTask(task1);
        Task task2 = new Task("Task #2", "Task2 description", TaskStatus.IN_PROGRESS);
        final int taskId2 = manager.addNewTask(task2);

        Epic epic1 = new Epic("Epic #1", "Epic1 description");
        final int epicId1 = manager.addNewEpic(epic1);
        Epic epic2 = new Epic("Epic #2", "Epic2 description");
        final int epicId2 = manager.addNewEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask #1-1", "Subtask1 description", TaskStatus.NEW, epicId1);
        final int subtaskId1 = manager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask #2-1", "Subtask2 description", TaskStatus.NEW, epicId1);
        final int subtaskId2 = manager.addNewSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask #1-2", "Subtask1 description", TaskStatus.NEW, epicId2);
        final int subtaskId3 = manager.addNewSubtask(subtask3);

        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubtaskList());

        // Обновление

        task1.setStatusOfTask(TaskStatus.IN_PROGRESS);
        manager.updateTask(task1);
        System.out.println("CHANGE STATUS: task1 NEW->IN_PROGRESS");
        task2.setStatusOfTask(TaskStatus.DONE);
        manager.updateTask(task2);
        System.out.println("CHANGE STATUS: task2 IN_PROGRESS->DONE");

        System.out.println("Задачи:");
        System.out.println(manager.getTaskList());


        subtask1.setStatusOfTask(TaskStatus.IN_PROGRESS);
        System.out.println("CHANGE STATUS: subtask1 NEW->IN_PROGRESS");
        manager.updateSubtask(subtask1);

        subtask2.setStatusOfTask(TaskStatus.DONE);
        System.out.println("CHANGE STATUS: subtask2 NEW->DONE");
        manager.updateSubtask(subtask2);

        subtask3.setStatusOfTask(TaskStatus.DONE);
        System.out.println("CHANGE STATUS: subtask3 NEW->DONE");
        manager.updateSubtask(subtask3);

        System.out.println("Подзадачи:");

        System.out.println(manager.getSubtaskList());


        System.out.println("Эпики:");

        System.out.println(manager.getEpicList());


        // Удаление

        System.out.println("DELETE: task1");

        manager.deleteTaskById(taskId1);

        System.out.println("DELETE: epic1");

        manager.deleteEpicById(epicId1);

        System.out.println(manager.getTaskList());
        System.out.println(manager.getEpicList());
        System.out.println(manager.getSubtaskList());

    }
}