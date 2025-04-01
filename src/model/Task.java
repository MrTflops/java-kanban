package model;


import java.util.Objects;

public class Task {
    protected String nameTask;
    protected String descriptionTask;
    protected TaskStatus statusOfTask;
    protected int id = 0;

    public Task(String nameTask, String descriptionTask, TaskStatus statusOfTask) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.statusOfTask = statusOfTask;
    }

    public Task(String nameTask, String descriptionTask) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.statusOfTask = TaskStatus.NEW;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public TaskStatus getStatusOfTask() {
        return statusOfTask;
    }

    public void setStatusOfTask(TaskStatus statusOfTask) {
        this.statusOfTask = statusOfTask;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "{ nameTask='" + nameTask
                + "', descriptionTask ='" + descriptionTask
                + "', statusOfTask='" + statusOfTask
                + "', id=" + id
                + '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}