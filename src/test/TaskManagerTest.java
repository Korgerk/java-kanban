package test;

import main.Subtask;
import main.Task;
import main.TaskManager;
import main.Status;
import main.Epic;

import java.util.ArrayList;

public class TaskManagerTest {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();


        // Полный цикл тестов test.TaskTest
        Task task1 = taskManager.addTask(new Task("Задача 1", "Описание 1"));
        Task task2 = taskManager.addTask(new Task("Задача 2", "Описание 2"));
        System.out.println("Созданы две задачи:");
        System.out.println(task1);
        System.out.println(task2);
        System.out.println();

        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        System.out.println("Обновлён статус первой задачи:");
        System.out.println(task1);
        System.out.println();

        taskManager.deleteTaskByID(task2.getId());
        System.out.println("Удалена вторая задача.");
        System.out.println();


        // Полный цикл тестов SubtaskTest
        // Создание эпиков перед добавлением подзадач
        Epic epicForSubtasks = taskManager.addEpic(new Epic("Эпик", "Описание"));

        // Добавление подзадач с корректным epicID
        Subtask subtask1 = taskManager.addSubtask(new Subtask("Подзадача 1", "Описание 1", epicForSubtasks.getId()));
        Subtask subtask2 = taskManager.addSubtask(new Subtask("Подзадача 2", "Описание 2", epicForSubtasks.getId()));
        Subtask subtask3 = taskManager.addSubtask(new Subtask("Подзадача 3", "Описание 3", epicForSubtasks.getId()));
        System.out.println("Созданы три подзадачи:");
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(subtask3);
        System.out.println();

        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask2);
        System.out.println("Обновлён статус второй подзадачи:");
        System.out.println(subtask2);
        System.out.println();

        taskManager.deleteSubtaskByID(subtask3.getId());
        System.out.println("Удалена третья подзадача.");
        System.out.println();

        taskManager.deleteEpicByID(epicForSubtasks.getId());

        // Полный цикл тестов test.EpicTest
        Epic epic1 = taskManager.addEpic(new Epic("Эпик 1", "Описание 1"));
        Epic epic2 = taskManager.addEpic(new Epic("Эпик 2", "Описание 2"));
        System.out.println("Созданы два эпика:");
        System.out.println(epic1);
        System.out.println(epic2);
        System.out.println();

        taskManager.addSubtask(new Subtask("Подзадача 1", "Описание 1", epic1.getId()));
        taskManager.addSubtask(new Subtask("Подзадача 2", "Описание 2", epic1.getId()));
        System.out.println("Добавлены две подзадачи в первый эпик:");
        System.out.println(epic1);
        System.out.println();

        taskManager.addSubtask(new Subtask("Подзадача 3", "Описание 3", epic2.getId()));
        taskManager.addSubtask(new Subtask("Подзадача 4", "Описание 4", epic2.getId()));
        taskManager.addSubtask(new Subtask("Подзадача 5", "Описание 5", epic2.getId()));
        System.out.println("Добавлены три подзадачи во второй эпик:");
        System.out.println(epic2);
        System.out.println();

        ArrayList<Subtask> subtasks = new ArrayList<>(taskManager.getEpicSubtasks(epic2)); // Создаём копию списка
        for (Subtask subtask : subtasks) {
            subtask.setStatus(Status.DONE);
            taskManager.updateSubtask(subtask);
        }
        System.out.println("Изменён статус всех подзадач во втором эпике:");
        System.out.println(epic2);
        System.out.println();

        taskManager.deleteEpicByID(epic1.getId());
        System.out.println("Удалён первый эпик.");
        System.out.println();

        System.out.println("Оставшиеся эпики:");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println();

        System.out.println("Состояние main.TaskManager:");
        System.out.println("Количество задач: " + taskManager.getTasks().size());
        System.out.println("Количество подзадач: " + taskManager.getSubtasks().size());
        System.out.println("Количество эпиков: " + taskManager.getEpics().size());
        System.out.println();


        // Удаление всех элементов
        taskManager.deleteTasks();
        taskManager.deleteSubtasks();
        taskManager.deleteEpics();

        System.out.println("Финальное состояние main.TaskManager:");
        System.out.println("Количество задач: " + taskManager.getTasks().size());
        System.out.println("Количество подзадач: " + taskManager.getSubtasks().size());
        System.out.println("Количество эпиков: " + taskManager.getEpics().size());
        System.out.println();
    }
}