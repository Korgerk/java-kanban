package tests.test.manager;

import main.manager.Managers;
import main.manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void testHistoryAfterGettingTasks() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager.getTaskByID(task1.getId());
        taskManager.getTaskByID(task2.getId());

        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size());
        assertTrue(history.contains(task1));
        assertTrue(history.contains(task2));
    }

    @Test
    void testHistoryAfterDeletingTask() {
        Task task = new Task("Task 1", "Description 1");
        taskManager.addTask(task);

        taskManager.getTaskByID(task.getId());
        taskManager.deleteTaskByID(task.getId());

        List<Task> history = taskManager.getHistory();
        assertTrue(history.isEmpty());
    }
}