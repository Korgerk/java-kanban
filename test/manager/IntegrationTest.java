package test.manager;

import main.manager.FileBackedTaskManager;
import main.manager.TaskManager;
import main.status.Status;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {
    private TaskManager manager;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("test", ".csv");
        manager = FileBackedTaskManager.loadFromFile(tempFile);
    }

    @Test
    void testSaveAndLoadTasks() {
        Task task = new Task("Задача", "Описание", Status.NEW);
        manager.addTask(task);

        ((FileBackedTaskManager) manager).save();
        manager = FileBackedTaskManager.loadFromFile(tempFile);

        Task loaded = manager.getTaskByID(task.getId());
        assertNotNull(loaded);
        assertEquals(task.getName(), loaded.getName());
    }

    @Test
    void testSaveAndLoadEpicsWithSubtasks() {
        Epic epic = new Epic("Эпик", "Описание", Status.NEW);
        manager.addEpic(epic);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание", epic.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание", epic.getId());

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        ((FileBackedTaskManager) manager).save();
        manager = FileBackedTaskManager.loadFromFile(tempFile);

        Epic loadedEpic = manager.getEpicByID(epic.getId());
        assertNotNull(loadedEpic);
        assertEquals(2, loadedEpic.getSubtaskList().size());
    }

    @Test
    void testClearAndReload() {
        Task task = new Task("Задача", "Описание", Status.NEW);
        manager.addTask(task);
        manager.clearAll();

        ((FileBackedTaskManager) manager).save();
        manager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(manager.getTasks().isEmpty());
    }
}