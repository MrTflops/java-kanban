package model;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    //Метод для установки ID
    public void setId(int id) {
        super.setId(id);
    }
}