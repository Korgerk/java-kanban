package main;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtaskList = new ArrayList<>(); // список задач

    public Epic(String name, String description) { // статус новый
        super(name, description);
    }

    public Epic(int id, String name, String description, Status status) { // конструктор всего
        super(id, name, description, status);
    }

    public void addSubtask(Subtask subtask) { // добавление подзадачи
        subtaskList.add(subtask);
    }

    public void clearSubtasks() { // очистка подзадач
        subtaskList.clear();
    }

    public ArrayList<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(ArrayList<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
    }

    @Override
    public String toString() {
        return "main.Epic{" +
                "name= " + getName() + '\'' +
                ", description = " + getDescription() + '\'' +
                ", id=" + getId() +
                ", subtaskList.size = " + subtaskList.size() +
                ", status = " + getStatus() +
                '}';
    }
}