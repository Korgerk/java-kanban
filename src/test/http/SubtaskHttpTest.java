package test.http;

import com.google.gson.Gson;
import main.http.server.HttpTaskServer;
import main.tasks.Epic;
import main.tasks.Subtask;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SubtaskHttpTest extends HttpTaskServerTest {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = HttpTaskServer.getGson();

    @Test
    void shouldCreateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача", "Описание", epic.getId());
        String json = gson.toJson(subtask);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks")).POST(HttpRequest.BodyPublishers.ofString(json)).header("Content-Type", "application/json").build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(1, taskManager.getSubtasks().size());
    }

    @Test
    void shouldDeleteSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача", "Описание", epic.getId());
        taskManager.addSubtask(subtask);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks/" + subtask.getId())).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNull(taskManager.getSubtaskByID(subtask.getId()));
    }

    @Test
    void shouldReturn404IfSubtaskNotFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks/999")).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}