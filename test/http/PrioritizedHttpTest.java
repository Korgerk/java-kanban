package test.http;

import main.status.Status;
import main.tasks.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrioritizedHttpTest extends HttpTaskServerTest {
    private final HttpClient client = HttpClient.newHttpClient();

    @Test
    void shouldGetPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        task1.setStartTime(java.time.LocalDateTime.now().minusHours(1));
        task1.setDuration(Duration.ofMinutes(30));

        Task task2 = new Task("Задача 2", "Описание задачи 2", Status.NEW);
        task2.setStartTime(java.time.LocalDateTime.now().plusHours(1));
        task2.setDuration(Duration.ofMinutes(30));

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/prioritized")).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Задача 1"));
        assertTrue(response.body().contains("Задача 2"));
    }
}