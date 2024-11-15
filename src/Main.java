package main;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        // Используем утилитарный класс Managers для получения менеджера задач
        TaskManager manager = Managers.getDefault();

        // Создание задач
        Task task = new Task("Test Task", "Simple task", Status.NEW);
        Epic epic = new Epic("Epic Task", "Big task with subtasks");
        Subtask subtask1 = new Subtask("Subtask 1", "Part 1", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Part 2", Status.NEW, epic.getId());

        // Добавление задач в менеджер
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        // Обращение к задачам для тестирования истории просмотров
        System.out.println("Просмотр задачи: " + manager.getTask(task.getId()));
        System.out.println("Просмотр эпика: " + manager.getEpic(epic.getId()));
        System.out.println("Просмотр подзадачи 1: " + manager.getSubtask(subtask1.getId()));
        System.out.println("Просмотр подзадачи 2: " + manager.getSubtask(subtask2.getId()));

        // Вывод истории просмотров
        System.out.println("\nИстория просмотров:");
        manager.getHistory().forEach(System.out::println);
    }
}
