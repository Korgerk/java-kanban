package main.tasks;

import main.status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtaskList = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }


    public ArrayList<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(ArrayList<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
    }

    public void clearSubtasks() {
        subtaskList.clear();
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        throw new UnsupportedOperationException("Нельзя установить startTime напрямую у эпика");
    }

    @Override
    public void setDuration(Duration duration) {
        throw new UnsupportedOperationException("Нельзя установить duration напрямую у эпика");
    }

    @Override
    public String toString() {
        return String.format("%s (ID: %d, Статус: %s, Подзадач: %d)", getName(), getId(), getStatus(), getSubtaskList().size());
    }
}