package test.http;

import com.google.gson.Gson;
import main.http.server.HttpTaskServer;
import main.status.Status;
import main.tasks.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class TaskHttpTest extends HttpTaskServerTest {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = HttpTaskServer.getGson();

    @Test
    void shouldGetAllTasks() throws IOException, InterruptedException {
        Task task = new Task("Задача", "Описание", Status.NEW);
        task.setStartTime(java.time.LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(30));
        taskManager.addTask(task);

        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Задача"));
    }

    @Test
    void shouldCreateTask() throws IOException, InterruptedException {
        Task task = new Task("Задача", "Описание", Status.NEW);
        String json = gson.toJson(task);

        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).POST(HttpRequest.BodyPublishers.ofString(json)).header("Content-Type", "application/json").build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    void shouldUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("Задача 1", "Описание 1", Status.NEW);
        taskManager.addTask(task);

        Task savedTask = taskManager.getTaskByID(task.getId());
        savedTask.setName("Задача 2");

        String json = gson.toJson(savedTask);

        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).POST(HttpRequest.BodyPublishers.ofString(json)).header("Content-Type", "application/json").build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals("Задача 2", taskManager.getTaskByID(savedTask.getId()).getName());
    }

    @Test
    void shouldDeleteTaskById() throws IOException, InterruptedException {
        Task task = new Task("Задача", "Описание", Status.NEW);
        taskManager.addTask(task);

        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/" + task.getId())).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNull(taskManager.getTaskByID(task.getId()));
    }

    @Test
    void shouldReturn404IfTaskNotFound() throws IOException, InterruptedException {
        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/999")).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}