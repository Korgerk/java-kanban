package test.tasks;

import main.manager.Managers;
import main.manager.TaskManager;
import main.status.Status;
import main.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TaskTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void testAddAndGetTask() {
        Task task = new Task("Задача", "Описание задачи");
        taskManager.addTask(task);
        assertEquals(task, taskManager.getTaskByID(task.getId()));
    }

    @Test
    void testUpdateTaskStatus() {
        Task task = new Task("Задача", "Описание задачи");
        taskManager.addTask(task);
        task.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task);
        assertEquals(Status.IN_PROGRESS, taskManager.getTaskByID(task.getId()).getStatus());
    }

    @Test
    void testDeleteTaskByID() {
        Task task = new Task("Задача", "Описание задачи");
        taskManager.addTask(task);
        taskManager.deleteTaskByID(task.getId());
        assertNull(taskManager.getTaskByID(task.getId()));
    }
}