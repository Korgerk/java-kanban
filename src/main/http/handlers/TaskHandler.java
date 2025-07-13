package main.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;
import main.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(gson);
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            switch (method) {
                case "GET":
                    if (path.equals("/tasks")) {
                        List<Task> tasks = taskManager.getTasks();
                        sendText(exchange, gson.toJson(tasks), 200);
                    } else if (path.matches("/tasks/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        Task task = taskManager.getTaskByID(id);
                        if (task == null) {
                            sendNotFound(exchange, "Задача не найдена");
                        } else {
                            sendText(exchange, gson.toJson(task), 200);
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Task task = gson.fromJson(body, Task.class);

                    if (task.getId() == 0) {
                        if (taskManager.isOverlapping(task)) {
                            sendHasInteractions(exchange);
                        } else {
                            taskManager.addTask(task);
                            sendSuccess(exchange, "Задача создана", 201);
                        }
                    } else {
                        if (taskManager.updateTask(task) == null) {
                            sendNotFound(exchange, "Задача не найдена");
                        } else {
                            sendSuccess(exchange, "Задача обновлена", 201);
                        }
                    }
                    break;
                case "DELETE":
                    if (path.matches("/tasks/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        taskManager.deleteTaskByID(id);
                        sendSuccess(exchange, "Задача удалена", 200);
                    }
                    break;
                default:
                    sendText(exchange, """
                            {"error": "Метод не поддерживается"}""", 405);
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }
}