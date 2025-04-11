package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
    }

    public List<Integer> getSubtaskIds() {
        return new ArrayList<>(subtaskIds);
    }

    public boolean addSubtaskId(int subtaskId) {
        if (subtaskId == getId() || subtaskId <= 0) {  // Используем getter вместо прямого доступа к id
            return false;
        }
        if (!subtaskIds.contains(subtaskId)) {
            subtaskIds.add(subtaskId);
            return true;
        }
        return false;
    }

    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove((Integer) subtaskId);
    }

    public void clearSubtaskIds() {
        subtaskIds.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtaskIds=" + subtaskIds +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                '}';
    }
}