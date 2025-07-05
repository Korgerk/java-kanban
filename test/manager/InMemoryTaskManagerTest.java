package test.manager;

import main.manager.InMemoryHistoryManager;
import main.manager.InMemoryTaskManager;
import main.tasks.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager createManager() {
        return new InMemoryTaskManager(new InMemoryHistoryManager());
    }

    @Test
    void testCheckOverlap() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setDuration(Duration.ofMinutes(60));
        task1.setStartTime(LocalDateTime.now());

        Task task2 = new Task("Задача 2", "Описание задачи 2");
        task2.setDuration(Duration.ofMinutes(60));
        task2.setStartTime(LocalDateTime.now().plusMinutes(30));

        taskManager.addTask(task1);
        assertTrue(taskManager.isOverlapping(task2));
    }

    @Test
    void testNoOverlap() {
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        task1.setDuration(Duration.ofMinutes(60));
        task1.setStartTime(LocalDateTime.now());

        Task task2 = new Task("Задача 2", "Описание задачи 2");
        task2.setDuration(Duration.ofMinutes(60));
        task2.setStartTime(LocalDateTime.now().plusHours(2));

        taskManager.addTask(task1);
        assertFalse(taskManager.isOverlapping(task2));
    }
}
