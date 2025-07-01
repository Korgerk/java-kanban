package main.manager;

import main.status.Status;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
        loadFromFile();
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
    public void clearHistory() {
        super.clearHistory();
        save();
    }

    @Override
    public void clearAll() {
        super.clearAll();
        save();
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic");
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
            return String.format("%d,EPIC,%s,%s,%s,", epic.getId(), epic.getName(), epic.getStatus(), epic.getDescription());
        } else if (task instanceof Subtask subtask) {
            return String.format("%d,SUBTASK,%s,%s,%s,%d", subtask.getId(), subtask.getName(), subtask.getStatus(), subtask.getDescription(), subtask.getEpicID());
        } else {
            return String.format("%d,TASK,%s,%s,%s,", task.getId(), task.getName(), task.getStatus(), task.getDescription());
        }
    }

    private void loadFromFile() {
        try {
            if (!file.exists()) return;
            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.size() <= 1) return;

            List<Task> tasks = new ArrayList<>();
            List<Epic> epics = new ArrayList<>();
            List<Subtask> subtasks = new ArrayList<>();

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                Task task = fromString(line);
                if (task == null) continue;

                if (task instanceof Epic) {
                    epics.add((Epic) task);
                } else if (task instanceof Subtask) {
                    subtasks.add((Subtask) task);
                } else {
                    tasks.add(task);
                }
            }

            for (Epic epic : epics) {
                super.addEpic(epic);
            }

            for (Subtask subtask : subtasks) {
                super.addSubtask(subtask);
            }

            for (Task task : tasks) {
                super.addTask(task);
            }

        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка при загрузке файла: " + exception.getMessage());
        }
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
                return new Task(id, name, description, status);
            case "EPIC":
                return new Epic(id, name, description, status);
            case "SUBTASK":
                int epicId = Integer.parseInt(parts[5]);
                return new Subtask(id, name, description, status, epicId);
            default:
                return null;
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        return new FileBackedTaskManager(Managers.getDefaultHistory(), file);
    }
}