package main.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;
import main.tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
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
                    if (path.equals("/subtasks")) {
                        List<Subtask> subtasks = taskManager.getSubtasks();
                        sendText(exchange, gson.toJson(subtasks), 200);
                    } else if (path.matches("/subtasks/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        Subtask subtask = taskManager.getSubtaskByID(id);
                        if (subtask == null) {
                            sendNotFound(exchange, "Подзадача не найдена");
                        } else {
                            sendText(exchange, gson.toJson(subtask), 200);
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Subtask subtask = gson.fromJson(body, Subtask.class);

                    if (subtask.getId() == 0) {
                        if (taskManager.isOverlapping(subtask)) {
                            sendHasInteractions(exchange);
                        } else {
                            taskManager.addSubtask(subtask);
                            sendSuccess(exchange, "Подзадача создана", 201);
                        }
                    } else {
                        if (taskManager.updateSubtask(subtask) == null) {
                            sendNotFound(exchange, "Подзадача не найдена");
                        } else {
                            sendSuccess(exchange, "Подзадача обновлена", 201);
                        }
                    }
                    break;
                case "DELETE":
                    if (path.matches("/subtasks/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        taskManager.deleteSubtaskByID(id);
                        sendSuccess(exchange, "Подзадача удалена", 200);
                    }
                    break;
                default:
                    sendText(exchange, "{\"error\":\"Метод не поддерживается\"}", 405);
                    // sendText(exchange, """
                    //         {"error": "Метод не поддерживается"}""", 405);
                    // Не хочет проходить Checkstyle. Leading braces [RegexpSinglelineJava]
            }
        } catch (Exception e) {
            sendServerError(exchange, e);
        }
    }
}