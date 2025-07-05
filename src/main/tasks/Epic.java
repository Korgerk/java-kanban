package main.tasks;

import main.status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtaskList = new ArrayList<>();
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;

    public Epic(String name, String description) {
        super(name, description);
        this.duration = Duration.ofMinutes(0);
        this.startTime = null;
        this.endTime = null;
    }

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        this.duration = Duration.ofMinutes(0);
        this.startTime = null;
        this.endTime = null;
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
        this.duration = Duration.ofMinutes(0);
        this.startTime = null;
        this.endTime = null;
    }

    public void updateDurationAndTimes() {
        if (subtaskList.isEmpty()) {
            this.duration = null;
            this.startTime = null;
            this.endTime = null;
            return;
        }

        LocalDateTime earliestStart = null;
        LocalDateTime latestEnd = null;
        long totalMinutes = 0;

        for (Subtask subtask : subtaskList) {
            if (subtask.getStartTime() == null || subtask.getDuration() == null) continue;

            totalMinutes += subtask.getDuration().toMinutes();
            if (earliestStart == null || subtask.getStartTime().isBefore(earliestStart)) {
                earliestStart = subtask.getStartTime();
            }
            LocalDateTime end = subtask.getEndTime();
            if (end != null && (latestEnd == null || end.isAfter(latestEnd))) {
                latestEnd = end;
            }
        }

        this.duration = Duration.ofMinutes(totalMinutes);
        this.startTime = earliestStart;
        this.endTime = latestEnd;
    }

    public ArrayList<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(ArrayList<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
        updateDurationAndTimes();
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
        updateDurationAndTimes();
    }

    public void clearSubtasks() {
        subtaskList.clear();
        updateDurationAndTimes();
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        throw new UnsupportedOperationException("Нельзя установить startTime напрямую у эпика");
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Duration getDuration() {
        return duration;
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