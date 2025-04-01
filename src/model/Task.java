package model;

import java.lang.reflect.Field;
import java.util.Objects;

public class Task {
    private static int idCounter = 1;
    private int id;   // убрал `final`, чтобы можно было изменять ID
    private String title;
    private String description;
    private Status status;

    public Task(String title, String description, Status status) {
        this.id = idCounter++;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    // ✅ Метод для установки ID
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
