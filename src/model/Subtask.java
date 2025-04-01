package model;

import java.lang.reflect.Field;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    // рефлексируем
    public void setId(int id) {
        try {
            Field field = Task.class.getDeclaredField("id");
            field.setAccessible(true);
            field.setInt(this, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
