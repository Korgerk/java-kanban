package test;

import main.Subtask;
import main.TaskManager;
import main.Status;
import main.Epic;
import java.util.ArrayList;

public class EpicTest {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // Создание эпика 1
        Epic epic1 = taskManager.addEpic(new Epic("Эпик 1", "Описание эпика 1"));
        System.out.println("Создан первый эпик:");
        System.out.println(epic1);
        System.out.println();

        // Создание эпика 2
        Epic epic2 = taskManager.addEpic(new Epic("Эпик 2", "Описание эпика 2"));
        System.out.println("Создан второй эпик:");
        System.out.println(epic2);
        System.out.println();

        // Добавление в первый эпик подзадач
        System.out.println("Добавляем две подзадачи в первый эпик...");
        taskManager.addSubtask(new Subtask("Подзадача 1", "Описание подзадачи 1", epic1.getId()));
        taskManager.addSubtask(new Subtask("Подзадача 2", "Описание подзадачи 2", epic1.getId()));
        System.out.println("Подзадачи успешно добавлены.");
        System.out.println(epic1);
        System.out.println();

        // Добавление подзадач во второй эпик
        System.out.println("Добавляем три подзадачи во второй эпик...");
        taskManager.addSubtask(new Subtask("Подзадача 3", "Описание подзадачи 3", epic2.getId()));
        taskManager.addSubtask(new Subtask("Подзадача 4", "Описание подзадачи 4", epic2.getId()));
        taskManager.addSubtask(new Subtask("Подзадача 5", "Описание подзадачи 5", epic2.getId()));
        System.out.println("Подзадачи успешно добавлены.");
        System.out.println(epic2);
        System.out.println();

        // Изменение статуса всех подзадач во втором эпике
        ArrayList<Subtask> subtasks = new ArrayList<>(taskManager.getEpicSubtasks(epic2)); // Создаём копию списка
        for (Subtask subtask : subtasks) {
            subtask.setStatus(Status.DONE);
            taskManager.updateSubtask(subtask);
        }
        System.out.println("Изменён статус подзадач во втором эпике");
        System.out.println(epic2);
        System.out.println();

        // Удаление первого эпика
        System.out.println("Удаляем эпик");
        taskManager.deleteEpicByID(epic1.getId());
        System.out.println();

        // Вывод всех оставшихся эпиков
        System.out.println("Оставшиеся эпики:");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        System.out.println();
    }
}