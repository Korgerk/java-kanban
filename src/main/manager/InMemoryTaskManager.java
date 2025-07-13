package main.manager;

import main.status.Status;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager;
    private final SortedSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));
    private int nextID = 1;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public void addTask(Task task) {
        task.setId(nextID++);
        tasks.put(task.getId(), task);
        historyManager.add(task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextID++);
        epics.put(epic.getId(), epic);
        historyManager.add(epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicID())) {
            throw new IllegalArgumentException("Эпик с ID " + subtask.getEpicID() + " не существует.");
        }
        subtask.setId(nextID++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicID());
        epic.addSubtask(subtask);
        updateEpicStatus(epic);
        historyManager.add(subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
    }

    @Override
    public Task getTaskByID(int id) {
        Task task = tasks.get(id);
        if (task != null) historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicByID(int id) {
        Epic epic = epics.get(id);
        if (epic != null) historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskByID(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) historyManager.add(subtask);
        return subtask;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void clearSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            epic.setStatus(Status.NEW);
        }
    }

    @Override
    public void deleteTaskByID(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicByID(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtaskList()) {
                subtasks.remove(subtask.getId());
                historyManager.remove(subtask.getId());
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtaskByID(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicID());
            if (epic != null) {
                epic.getSubtaskList().remove(subtask);
                updateEpicStatus(epic);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public Task updateTask(Task task) {
        Integer taskId = task.getId();
        if (taskId == null || !tasks.containsKey(taskId)) {
            return null;
        }
        tasks.put(taskId, task);
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Integer epicId = epic.getId();
        if (epicId == null || !epics.containsKey(epicId)) {
            return null;
        }
        Epic oldEpic = epics.get(epicId);
        ArrayList<Subtask> oldSubtasks = oldEpic.getSubtaskList();
        ArrayList<Subtask> newSubtasks = epic.getSubtaskList();

        for (Subtask subtask : oldSubtasks) {
            subtasks.remove(subtask.getId());
        }

        for (Subtask subtask : newSubtasks) {
            subtasks.put(subtask.getId(), subtask);
        }

        epics.put(epicId, epic);
        updateEpicStatus(epic);
        return epic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Integer subtaskId = subtask.getId();
        if (subtaskId == null || !subtasks.containsKey(subtaskId)) {
            return null;
        }
        Epic epic = epics.get(subtask.getEpicID());
        if (epic == null) {
            return null;
        }

        List<Subtask> subtaskList = epic.getSubtaskList();
        for (int i = 0; i < subtaskList.size(); i++) {
            if (subtaskList.get(i).getId() == subtaskId) {
                subtaskList.set(i, subtask);
                break;
            }
        }

        subtasks.put(subtaskId, subtask);
        updateEpicStatus(epic);
        return subtask;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public boolean isOverlapping(Task task) {
        LocalDateTime start = task.getStartTime();
        LocalDateTime end = task.getEndTime();
        if (start == null || end == null) return false;

        for (Task other : tasks.values()) {
            if (other == task) continue;

            LocalDateTime otherStart = other.getStartTime();
            LocalDateTime otherEnd = other.getEndTime();

            if (otherStart == null || otherEnd == null) continue;

            if (start.isBefore(otherEnd) && end.isAfter(otherStart)) {
                return true;
            }
        }

        for (Subtask subtask : subtasks.values()) {
            LocalDateTime otherStart = subtask.getStartTime();
            LocalDateTime otherEnd = subtask.getEndTime();

            if (otherStart == null || otherEnd == null) continue;

            if (start.isBefore(otherEnd) && end.isAfter(otherStart)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void clearAll() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
        historyManager.clearHistory();
    }

    @Override
    public void clearHistory() {
        historyManager.clearHistory();
    }

    private void updateEpicStatus(Epic epic) {
        boolean allDone = true;
        boolean allNew = true;
        for (Subtask subtask : epic.getSubtaskList()) {
            if (subtask.getStatus() != Status.DONE) allDone = false;
            if (subtask.getStatus() != Status.NEW) allNew = false;
        }

        if (epic.getSubtaskList().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else if (allNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}