package Main.tasks;

import Main.status.Status;

public class Subtask extends Task {
    private int epicID; // Убираем final

    public Subtask(String name, String description, int epicID) {
        super(name, description);
        this.epicID = epicID;
    }

    public Subtask(int id, String name, String description, Status status, int epicID) {
        super(id, name, description, status);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) { // Добавляем метод setEpicID
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        return "Main.tasks.Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", epicID=" + epicID +
                ", Main.status=" + getStatus() +
                '}';
    }
}