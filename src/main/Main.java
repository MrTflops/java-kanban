package main;

import tasktracker.*;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Создание задач
        Task task = new Task("Test tasktracker.Task", "Simple task", Status.NEW);
        Epic epic = new Epic("tasktracker.Epic tasktracker.Task", "Big task with subtasks");
        Subtask subtask1 = new Subtask("tasktracker.Subtask 1", "Part 1", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("tasktracker.Subtask 2", "Part 2", Status.NEW, epic.getId());

        // Добавление задач в менеджер
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        // Проверка статуса эпика
        System.out.println("tasktracker.Epic status: " + epic.getStatus());
    }
}
