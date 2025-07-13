package main.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.manager.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(gson);
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod()))
        {
            sendText(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
        } else
        {
            sendText(exchange, """
                {"error": "Метод не поддерживается"}""", 405);
        }
    }
}