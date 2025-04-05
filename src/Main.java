package main;

import model.Status;
import model.Task;

public class Main {
    public static void main(String[] args) {
        HistoryManager historyManager = new InMemoryHistoryManager();

        // создаем задачи
        Task task1 = new Task("Task 1", "Description 1", Status.NEW);
        Task task2 = new Task("Task 2", "Description 2", Status.IN_PROGRESS);
        Task task3 = new Task("Task 3", "Description 3", Status.DONE);


        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        // повторное добавление task2 (по идее должно переместиться в конец)
        historyManager.add(task2);

        // выводим истории
        System.out.println("История просмотров:");
        historyManager.getHistory().forEach(System.out::println);

        // удаляем
        historyManager.remove(task1.getId());

        // вывод после удаления
        System.out.println("\nИстория после удаления:");
        historyManager.getHistory().forEach(System.out::println);
    }
}