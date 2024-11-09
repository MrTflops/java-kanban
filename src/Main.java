import main.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Создание задач
        Task task = new Task("Test model.Task", "Simple task", Status.NEW);
        Epic epic = new Epic("model.Epic model.Task", "Big task with subtasks");
        Subtask subtask1 = new Subtask("model.Subtask 1", "Part 1", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("model.Subtask 2", "Part 2", Status.NEW, epic.getId());

        // Добавление задач в менеджер
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        // Проверка статуса эпика
        System.out.println("model.Epic status: " + epic.getStatus());
    }
}
