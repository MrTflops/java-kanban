package main;

import model.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private static final String HEADER = "id,type,name,status,description,duration,startTime,endTime,epicId";

    public FileBackedTaskManager(File file) {
        super(); // Вызываем конструктор родительского класса
        this.file = file;
    }

    private void save() {
        try (Writer writer = new FileWriter(file)) {
            writer.write(HEADER + "\n"); // Записываем заголовок

            for (Task task : tasks.values()) {
                writer.write(toString(task) + "\n");
            }

            for (Epic epic : epics.values()) {
                writer.write(toString(epic) + "\n");
            }

            for (Subtask subtask : subtasks.values()) {
                writer.write(toString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении в файл", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;

                Task task = fromString(line);

                if (task instanceof Epic) {
                    manager.epics.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    Subtask subtask = (Subtask) task;
                    manager.subtasks.put(subtask.getId(), subtask);


                    Epic epic = manager.epics.get(subtask.getEpicId());
                    if (epic != null) {
                        epic.addSubtaskId(subtask.getId());
                    }
                } else {
                    manager.tasks.put(task.getId(), task);
                }


                if (task.getId() >= manager.nextId) {
                    manager.nextId = task.getId() + 1;
                }
            }


            for (Epic epic : manager.epics.values()) {
                manager.updateEpicStatus(epic);
                manager.updateEpicTimes(epic);
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке из файла", e);
        }

        return manager;
    }


    private String toString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",")
                .append(getType(task)).append(",")
                .append(task.getTitle()).append(",")
                .append(task.getStatus()).append(",")
                .append(task.getDescription()).append(",")
                .append(task.getDuration()).append(",")
                .append(task.getStartTime()).append(",")
                .append(task.getEndTime());


        if (task instanceof Subtask) {
            sb.append(",").append(((Subtask) task).getEpicId());
        } else {
            sb.append(",");
        }

        return sb.toString();
    }


    private static Type getType(Task task) {
        if (task instanceof Epic) return Type.EPIC;
        if (task instanceof Subtask) return Type.SUBTASK;
        return Type.TASK;
    }


    private static Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        Type type = Type.valueOf(parts[1]);
        String title = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        Duration duration = parts[5].isEmpty() ? null : Duration.parse(parts[5]);
        LocalDateTime startTime = parts[6].isEmpty() ? null : LocalDateTime.parse(parts[6]);
        LocalDateTime endTime = parts[7].isEmpty() ? null : LocalDateTime.parse(parts[7]);
        int epicId = parts.length > 8 && !parts[8].isEmpty() ? Integer.parseInt(parts[8]) : 0;


        switch (type) {
            case TASK:
                Task task = new Task(title, description, status, startTime, duration);
                task.setId(id);
                return task;
            case EPIC:
                Epic epic = new Epic(title, description);
                epic.setId(id);
                epic.setStatus(status);
                epic.setStartTime(startTime);
                epic.setDuration(duration);
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask(title, description, status, epicId, startTime, duration);
                subtask.setId(id);
                return subtask;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }



    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }
}