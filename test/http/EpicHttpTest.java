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

import static org.junit.jupiter.api.Assertions.*;

class EpicHttpTest extends HttpTaskServerTest {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = HttpTaskServer.getGson();

    @Test
    void shouldGetAllEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);

        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics")).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Эпик"));
    }

    @Test
    void shouldCreateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание");

        String json = gson.toJson(epic);

        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics")).POST(HttpRequest.BodyPublishers.ofString(json)).header("Content-Type", "application/json").build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(1, taskManager.getEpics().size());
    }

    @Test
    void shouldGetSubtasksOfEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);

        Subtask subtask = new Subtask("Подзадача", "Описание", epic.getId());
        taskManager.addSubtask(subtask);

        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks")).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Подзадача"));
    }

    @Test
    void shouldDeleteEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Эпик", "Описание");
        taskManager.addEpic(epic);

        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics/" + epic.getId())).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNull(taskManager.getEpicByID(epic.getId()));
    }
}