public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

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

        // Проверка статуса эпика
        System.out.println("Epic status: " + epic.getStatus());
    }
}
