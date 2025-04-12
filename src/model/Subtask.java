package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        validateEpicId(epicId);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, Status status,
                   int epicId, LocalDateTime startTime, Duration duration) {
        super(title, description, status, startTime, duration);
        validateEpicId(epicId);
        this.epicId = epicId;
    }

    private void validateEpicId(int epicId) {
        if (epicId <= 0) {
            throw new IllegalArgumentException("Epic ID must be positive");
        }
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                '}';
    }
}
