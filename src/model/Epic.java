package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Subtask> subtaskList = new ArrayList<>();

    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
        this.statusOfTask = TaskStatus.NEW;

    }

    public void addNewSubtaskOnList(Subtask subtask) {
        subtaskList.add(subtask);
    }

    public void deleteSubtaskFromList(Subtask subtask) {
        subtaskList.remove(subtask);
    }

    public void clearSubtaskFromList() {
        subtaskList.clear();
    }

    public  void updateSubtaskList(Subtask subtask) {
        subtaskList.remove(subtask);
        subtaskList.add(subtask);
    }

    public void updateEpicStatus() {
        int countDone = 0;
        int countNew = 0;
        if (subtaskList.isEmpty()) {
            setStatusOfTask(TaskStatus.NEW);
        } else {
            for (Subtask subtask : subtaskList) {
                if (subtask.statusOfTask == TaskStatus.DONE) {
                    countDone++;
                } else if (subtask.statusOfTask == TaskStatus.NEW) {
                    countNew++;
                }
            }
            if (countNew == subtaskList.size()) {
                setStatusOfTask(TaskStatus.NEW);
            } else if (countDone == subtaskList.size()) {
                setStatusOfTask(TaskStatus.DONE);
            } else {
                setStatusOfTask(TaskStatus.IN_PROGRESS);
            }
        }
    }

    public ArrayList<Subtask> getSubtaskList() {
        return subtaskList;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "{ nameTask='" + nameTask
                + "', descriptionTask ='" + descriptionTask
                + "', statusOfTask='" + statusOfTask
                + "', id=" + id
                + "', subtaskList.size=" + subtaskList.size()
                + '}';
    }
}