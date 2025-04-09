package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import model.Type;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    private void save() {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");

            for (Task task : getAllTasks()) {
                writer.write(taskToString(task) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл", e);
        }
    }

    public static String taskToString(Task task) {
        Type type;
        String epicId = "";

        if (task instanceof Epic) {
            type = Type.EPIC;
        } else if (task instanceof Subtask) {
            type = Type.SUBTASK;
            epicId = String.valueOf(((Subtask) task).getEpicId());
        } else {
            type = Type.TASK;
        }

        return String.join(",",
                String.valueOf(task.getId()),
                type.name(),
                task.getTitle(),
                task.getStatus().name(),
                task.getDescription(),
                epicId);
    }

    public static Task taskFromString(String value) {
        String[] parts = value.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException(String.format(
                    "Некорректный формат строки. Ожидалось минимум 5 значений, получено %d: %s",
                    parts.length, value));
        }

        int id = Integer.parseInt(parts[0]);
        Type type = Type.valueOf(parts[1]);
        String title = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];

        switch (type) {
            case EPIC:
                Epic epic = new Epic(title, description);
                epic.setId(id);
                epic.setStatus(status);
                return epic;

            case SUBTASK:
                if (parts.length < 6) {
                    throw new IllegalArgumentException(String.format(
                            "Для подзадачи ожидалось 6 значений, получено %d: %s",
                            parts.length, value));
                }
                int epicId = Integer.parseInt(parts[5]);
                Subtask subtask = new Subtask(title, description, status, epicId);
                subtask.setId(id);
                return subtask;

            case TASK:
                Task task = new Task(title, description, status);
                task.setId(id);
                return task;

            default:
                throw new IllegalArgumentException(String.format(
                        "Неизвестный тип задачи: %s. Строка: %s",
                        type, value));
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try {
            String content = Files.readString(file.toPath());
            String[] lines = content.split("\n");

            if (lines.length == 0) {
                return manager;
            }

            // игнорим заголовок
            for (int i = 1; i < lines.length; i++) {
                if (lines[i].isEmpty()) {
                    continue;
                }

                Task task = taskFromString(lines[i]);

                if (task instanceof Epic) {
                    manager.addEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.addSubtask((Subtask) task);
                } else {
                    manager.addTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(String.format("Ошибка ввода-вывода при работе с файлом %s", file.getName()),
                    e
            );
        }

        return manager;
    }
}