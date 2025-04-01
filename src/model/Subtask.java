package model;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String nameTask, String descriptionTask, TaskStatus status, int epicId) {
        super(nameTask, descriptionTask, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "{ nameTask='" + nameTask
                + "', descriptionTask ='" + descriptionTask
                + "', statusOfTask='" + statusOfTask
                + "', id=" + id
                + "', epicId=" + epicId
                + '}';
    }
}