package main.http.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    protected final Gson gson;

    public BaseHttpHandler(Gson gson) {
        this.gson = gson;
    }

    protected void sendText(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h, String message) throws IOException {
        sendText(h, "{\"error\":\"" + message + "\"}", 404);
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        sendText(h, "{\"error\":\"Задача пересекается с существующими\"}", 406);
    }

    protected void sendServerError(HttpExchange h, Exception e) throws IOException {
        sendText(h, "{\"error\":\"" + e.getMessage() + "\"}", 500);
    }

    protected void sendSuccess(HttpExchange h, String message, int code) throws IOException {
        sendText(h, "{\"message\":\"" + message + "\"}", code);
    }
}