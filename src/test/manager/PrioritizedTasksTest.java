package test.manager;

import main.manager.Managers;
import main.manager.TaskManager;
import main.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrioritizedTasksTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void shouldSortTasksByStartTime() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setStartTime(LocalDateTime.now().plusMinutes(30));
        task1.setDuration(Duration.ofMinutes(15));

        Task task2 = new Task("Задача 2", "Описание задачи 2");
        task2.setStartTime(LocalDateTime.now());
        task2.setDuration(Duration.ofMinutes(15));

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        List<Task> prioritized = taskManager.getPrioritizedTasks();
        assertEquals(task2, prioritized.get(0));
        assertEquals(task1, prioritized.get(1));
    }

    @Test
    void shouldIgnoreTasksWithoutStartTime() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(15));

        Task task2 = new Task("Задача 2", "Описание задачи 2");

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        List<Task> prioritized = taskManager.getPrioritizedTasks();
        assertEquals(1, prioritized.size());
        assertEquals(task1, prioritized.get(0));
    }
}