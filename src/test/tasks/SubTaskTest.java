package test.tasks;

import main.manager.Managers;
import main.manager.TaskManager;
import main.status.Status;
import main.tasks.Epic;
import main.tasks.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SubTaskTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void testAddAndGetSubtask() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());
        taskManager.addSubtask(subtask);
        assertEquals(subtask, taskManager.getSubtaskByID(subtask.getId()));
    }

    @Test
    void testUpdateSubtaskStatus() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());
        taskManager.addSubtask(subtask);
        subtask.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        assertEquals(Status.IN_PROGRESS, taskManager.getSubtaskByID(subtask.getId()).getStatus());
    }

    @Test
    void testDeleteSubtaskByID() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача", "Описание подзадачи", epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.deleteSubtaskByID(subtask.getId());
        assertNull(taskManager.getSubtaskByID(subtask.getId()));
    }
}