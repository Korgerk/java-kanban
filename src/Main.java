import main.manager.FileBackedTaskManager;
import main.manager.Managers;
import main.manager.TaskManager;
import main.status.Status;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        File tempFile = File.createTempFile("tasks", ".csv");
        System.out.println("Файл создан: " + tempFile.getAbsolutePath());

        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(tempFile);

        System.out.println("\n\nДобавляем задачи");
        Task task1 = new Task("Задача 1", "Описание задачи 1");
        Task task2 = new Task("Задача 2", "Описание задачи 2");
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", epic1.getId());
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", epic1.getId());
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);


        printAll(manager);


        System.out.println("\n\nОбновляем статусы");
        task1.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task1);
        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);
        printAll(manager);

        System.out.println("\n\nУдаляем задачи");
        manager.deleteTaskByID(task2.getId());
        manager.deleteSubtaskByID(subtask2.getId());
        printAll(manager);

        System.out.println("\n\nСохраняем и перезагружаем");
        manager.save();
        manager = FileBackedTaskManager.loadFromFile(tempFile);
        printAll(manager);

        System.out.println("\n\nУдаляем всё");
        manager.clearAll();
        printAll(manager);
    }

    private static void printAll(TaskManager manager) {
        System.out.println("\n---ЗАДАЧИ---");
        for (Task t : manager.getTasks()) {
            System.out.println(t);
        }

        System.out.println("\n---ЭПИКИ---");
        for (Epic e : manager.getEpics()) {
            System.out.println(e);
            System.out.println("---ПОДЗАДАЧИ---");
            for (Subtask s : e.getSubtaskList()) {
                System.out.println("  - " + s);
            }
        }

        System.out.println("\n---ИСТОРИЯ ПРОСМОТРА---");
        List<Task> history = manager.getHistory();
        for (int i = 0; i < history.size(); i++) {
            System.out.println((i + 1) + ". " + history.get(i));
        }
    }
}