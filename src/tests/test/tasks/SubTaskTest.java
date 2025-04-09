package tests.test.tasks;

import tasks.Epic;
import tasks.Subtask;
import manager.TaskManager;
import status.Status;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubTaskTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void testAddAndGetSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        taskManager.addSubtask(subtask);

        assertEquals(subtask, taskManager.getSubtaskByID(subtask.getId()));
    }

    @Test
    void testUpdateSubtaskStatus() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        taskManager.addSubtask(subtask);

        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask);

        assertEquals(Status.IN_PROGRESS, taskManager.getSubtaskByID(subtask.getId()).getStatus());
    }

    @Test
    void testDeleteSubtaskByID() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи 1", epic.getId());
        taskManager.addSubtask(subtask);

        taskManager.deleteSubtaskByID(subtask.getId());
        assertNull(taskManager.getSubtaskByID(subtask.getId()));
    }
}