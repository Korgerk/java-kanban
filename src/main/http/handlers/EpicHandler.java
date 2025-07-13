package main.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;
import main.tasks.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager, Gson gson) {
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
                    if (path.equals("/epics")) {
                        List<Epic> epics = taskManager.getEpics();
                        sendText(exchange, gson.toJson(epics), 200);
                    } else if (path.matches("/epics/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        Epic epic = taskManager.getEpicByID(id);
                        if (epic == null) {
                            sendNotFound(exchange, "Эпик не найден");
                        } else {
                            sendText(exchange, gson.toJson(epic), 200);
                        }
                    } else if (path.matches("/epics/\\d+/subtasks")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        Epic epic = taskManager.getEpicByID(id);
                        if (epic == null) {
                            sendNotFound(exchange, "Эпик не найден");
                        } else {
                            sendText(exchange, gson.toJson(epic.getSubtaskList()), 200);
                        }
                    }
                    break;
                case "POST":
                    InputStream inputStream = exchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                    Epic epic = gson.fromJson(body, Epic.class);

                    if (epic.getId() == 0) {
                        taskManager.addEpic(epic);
                        sendSuccess(exchange, "Эпик создан", 201);
                    } else {
                        if (taskManager.updateEpic(epic) == null) {
                            sendNotFound(exchange, "Эпик не найден");
                        } else {
                            sendSuccess(exchange, "Эпик обновлён", 201);
                        }
                    }
                    break;
                case "DELETE":
                    if (path.matches("/epics/\\d+")) {
                        int id = Integer.parseInt(path.split("/")[2]);
                        taskManager.deleteEpicByID(id);
                        sendSuccess(exchange, "Эпик удалён", 200);
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