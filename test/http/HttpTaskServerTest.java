package http;

import com.google.gson.Gson;
import main.TaskManager;
import main.Managers;
import model.Task;
import model.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private HttpTaskServer server;
    private TaskManager taskManager;
    private Gson gson;
    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void setUp() throws IOException {
        taskManager = Managers.getDefault();
        server = new HttpTaskServer(taskManager);
        server.start();
        gson = HttpTaskServer.getGson();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Description", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getResponse.statusCode());
        assertTrue(getResponse.body().contains("Test Task"));
    }

    @Test
    void testGetPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Task 1", "Description", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.now().plusHours(1));
        Task task2 = new Task("Task 2", "Description", Status.NEW,
                Duration.ofMinutes(30), LocalDateTime.now());

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Task 2"));
        assertTrue(response.body().contains("Task 1"));
    }

    @Test
    void testGetHistory() throws IOException, InterruptedException {
        Task task = new Task("History Task", "Description", Status.NEW);
        taskManager.addTask(task);
        taskManager.getTask(task.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("History Task"));
    }
}