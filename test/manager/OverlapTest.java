package test.manager;

import main.manager.Managers;
import main.manager.TaskManager;
import main.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OverlapTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    void shouldDetectOverlap() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(60));

        Task task2 = new Task("Задача 2", "Описание задачи 2");
        task2.setStartTime(LocalDateTime.now().plusMinutes(30));
        task2.setDuration(Duration.ofMinutes(60));

        taskManager.addTask(task1);
        assertTrue(taskManager.isOverlapping(task2));
    }

    @Test
    void shouldNotDetectOverlap() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(60));

        Task task2 = new Task("Задача 2", "Описание задачи 2");
        task2.setStartTime(LocalDateTime.now().plusMinutes(60));
        task2.setDuration(Duration.ofMinutes(60));

        taskManager.addTask(task1);
        assertFalse(taskManager.isOverlapping(task2));
    }

    @Test
    void shouldNotCheckIfNoTimeOrDuration() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(60));

        Task task2 = new Task("Задача 2", "Описание задачи 1");

        taskManager.addTask(task1);
        assertFalse(taskManager.isOverlapping(task2));
    }
}