package test;

import main.Task;
import main.TaskManager;
import main.Status;

public class TaskTest {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Создание первой задачи
        Task task1 = taskManager.addTask(new Task("Задача 1", "Описание задачи 1"));
        System.out.println("Создана первая задача:");
        System.out.println(task1);
        System.out.println();

        // Обновление статуса задачи
        System.out.println("Обновляем статус задачи");
        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        System.out.println("Статус первой задачи успешно обновлен.");
        System.out.println(task1);
        System.out.println();

        // Создание задачи
        Task task2 = taskManager.addTask(new Task("Задача 2", "Описание задачи 2"));
        System.out.println("Создана вторая задача:");
        System.out.println(task2);
        System.out.println();

        // Удаление второй задачи
        System.out.println("Удаляем вторую задачу");
        taskManager.deleteTaskByID(task2.getId());
        System.out.println();
    }
}