package main.manager;

import main.status.Status;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getFileBackedManager(File file) {
        return new FileBackedTaskManager(getDefaultHistory(), file);
    }
}