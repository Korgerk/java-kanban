package main.manager;

import main.status.Status;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
        loadFromFile();
    }

    private static Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];

        switch (type) {
            case "TASK":
                Task task = new Task(id, name, description, status);
                if (parts.length > 6 && !parts[5].isEmpty()) {
                    task.setDuration(Duration.ofMinutes(Long.parseLong(parts[5])));
                }
                if (parts.length > 6 && !parts[6].isEmpty()) {
                    task.setStartTime(LocalDateTime.parse(parts[6]));
                }
                return task;

            case "EPIC":
                return new Epic(id, name, description, status);

            case "SUBTASK":
                int epicId = Integer.parseInt(parts[5]);
                Subtask subtask = new Subtask(id, name, description, status, epicId);
                if (parts.length > 6 && !parts[6].isEmpty()) {
                    subtask.setDuration(Duration.ofMinutes(Long.parseLong(parts[6])));
                }
                if (parts.length > 7 && !parts[7].isEmpty()) {
                    subtask.setStartTime(LocalDateTime.parse(parts[7]));
                }
                return subtask;

            default:
                return null;
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        return new FileBackedTaskManager(Managers.getDefaultHistory(), file);
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public Task updateTask(Task task) {
        Task result = super.updateTask(task);
        save();
        return result;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic result = super.updateEpic(epic);
        save();
        return result;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask result = super.updateSubtask(subtask);
        save();
        return result;
    }

    @Override
    public void deleteTaskByID(int id) {
        super.deleteTaskByID(id);
        save();
    }

    @Override
    public void deleteEpicByID(int id) {
        super.deleteEpicByID(id);
        save();
    }

    @Override
    public void deleteSubtaskByID(int id) {
        super.deleteSubtaskByID(id);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void clearAll() {
        super.clearAll();
        save();
    }

    @Override
    public void clearHistory() {
        super.clearHistory();
        save();
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic,duration,startTime");
            writer.newLine();

            for (Task task : getTasks()) {
                writer.write(taskToString(task));
                writer.newLine();
            }

            for (Epic epic : getEpics()) {
                writer.write(taskToString(epic));
                writer.newLine();

                for (Subtask subtask : epic.getSubtaskList()) {
                    writer.write(taskToString(subtask));
                    writer.newLine();
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при сохранении файла: " + exception.getMessage());
        }
    }

    private String taskToString(Task task) {
        if (task instanceof Epic epic) {
            return String.format("%d,EPIC,%s,%s,%s,,%s,%s", epic.getId(), epic.getName(), epic.getStatus(), epic.getDescription(), epic.getDuration() != null ? epic.getDuration().toMinutes() : "", epic.getStartTime() != null ? epic.getStartTime() : "");
        } else if (task instanceof Subtask subtask) {
            return String.format("%d,SUBTASK,%s,%s,%s,%d,%s,%s", subtask.getId(), subtask.getName(), subtask.getStatus(), subtask.getDescription(), subtask.getEpicID(), subtask.getDuration() != null ? subtask.getDuration().toMinutes() : "", subtask.getStartTime() != null ? subtask.getStartTime() : "");
        } else {
            return String.format("%d,TASK,%s,%s,%s,,%s,%s", task.getId(), task.getName(), task.getStatus(), task.getDescription(), task.getDuration() != null ? task.getDuration().toMinutes() : "", task.getStartTime() != null ? task.getStartTime() : "");
        }
    }

    private void loadFromFile() {
        try {
            if (!file.exists()) return;
            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.size() <= 1) return;

            Map<Integer, Epic> epics = new HashMap<>();
            Map<Integer, Subtask> subtasks = new HashMap<>();
            Map<Integer, Task> tasks = new HashMap<>();

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                Task task = fromString(line);
                if (task == null) continue;

                if (task instanceof Epic) {
                    epics.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    subtasks.put(task.getId(), (Subtask) task);
                } else {
                    tasks.put(task.getId(), task);
                }
            }

            for (Epic epic : epics.values()) {
                super.addEpic(epic);
            }

            for (Subtask subtask : subtasks.values()) {
                super.addSubtask(subtask);
            }

            for (Task task : tasks.values()) {
                super.addTask(task);
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при загрузке файла: " + exception.getMessage());
        }
    }
}