import Main.manager.Managers;
import Main.manager.TaskManager;
import Main.status.Status;
import Main.tasks.Epic;
import Main.tasks.Subtask;
import Main.tasks.Task;

public class Main {
    public static void main(String[] args) {
        // Создаем менеджер задач
        TaskManager taskManager = Managers.getDefault();

        // Создаем несколько задач разного типа
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", 0); // ID эпика будет установлен позже
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", 0); // ID эпика будет установлен позже

        // Добавляем задачи в менеджер
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager.addEpic(epic1);

        // Устанавливаем ID эпика для подзадач
        subtask1.setEpicID(epic1.getId());
        subtask2.setEpicID(epic1.getId());

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        // Выводим текущее состояние задач и историю
        System.out.println("-----Изначальное состояние-----");
        printAllTasks(taskManager);

        // Просматриваем задачи
        taskManager.getTaskByID(task1.getId());
        taskManager.getEpicByID(epic1.getId());
        taskManager.getSubtaskByID(subtask1.getId());

        // Выводим историю после просмотра
        System.out.println();
        System.out.println("-----После просмотра задач-----");
        printAllTasks(taskManager);

        // Обновляем статусы
        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);

        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);

        // Выводим состояние после обновления
        System.out.println();
        System.out.println("-----После обновления статусов-----");
        printAllTasks(taskManager);

        // Удаляем задачи
        taskManager.deleteTaskByID(task2.getId());
        taskManager.deleteSubtaskByID(subtask2.getId());

        // Выводим состояние после удаления
        System.out.println();
        System.out.println("-----После удаления задач-----");
        printAllTasks(taskManager);


        // Чистим всё. Также можно использовать clearAll();
        taskManager.clearSubtasks();

        System.out.println();
        System.out.println("-----После очистки подзадач-----");
        printAllTasks(taskManager);

        taskManager.clearTasks();

        System.out.println();
        System.out.println("-----После очистки задач-----");
        printAllTasks(taskManager);

        taskManager.clearEpics();

        System.out.println();
        System.out.println("-----После очистки эпиков-----");
        printAllTasks(taskManager);

        taskManager.clearHistory();

        System.out.println();
        System.out.println("-----После очистки истории(всего)-----");
        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }

        System.out.println("Эпики:");
        for (Epic epic : manager.getEpics()) {
            System.out.println(epic);

            for (Subtask subtask : manager.getSubtasks()) {
                if (subtask.getEpicID() == epic.getId()) {
                    System.out.println("--> " + subtask);
                }
            }
        }

        System.out.println("Подзадачи:");
        for (Subtask subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История просмотра:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}