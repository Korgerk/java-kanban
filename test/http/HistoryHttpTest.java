package test.http;

import main.status.Status;
import main.tasks.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HistoryHttpTest extends HttpTaskServerTest {
    private final HttpClient client = HttpClient.newHttpClient();

    @Test
    void shouldGetHistory() throws IOException, InterruptedException {
        Task task = new Task("Задача", "Описание", Status.NEW);
        taskManager.addTask(task);
        taskManager.getTaskByID(task.getId());

        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/history")).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Задача"));
    }
}