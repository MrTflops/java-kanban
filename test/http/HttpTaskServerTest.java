package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.adapter.DurationAdapter;
import http.adapter.LocalDateTimeAdapter;
import main.Managers;
import main.TaskManager;
import model.Status;
import model.Task;
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
    private static final int PORT = 8080;
    private HttpTaskServer server;
    private TaskManager manager;
    private Gson gson;
    private HttpClient client;

    @BeforeEach
    void setUp() throws IOException {
        manager = Managers.getDefault();
        server = new HttpTaskServer(manager);
        server.start();
        client = HttpClient.newHttpClient();

        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void testAddTask_ShouldReturn201Created() throws IOException, InterruptedException {
        // Arrange
        Task task = new Task("Test", "Description", Status.NEW);
        String taskJson = gson.toJson(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        // Act
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertAll(
                () -> assertEquals(201, response.statusCode(),
                        "При успешном создании задачи должен возвращаться статус 201"),
                () -> assertEquals(1, manager.getAllTasks().size(),
                        "В менеджере должна появиться новая задача"),
                () -> assertTrue(response.body().contains("Test"),
                        "Ответ должен содержать название созданной задачи")
        );
    }

    @Test
    void testAddTask_WithInvalidData_ShouldReturn400BadRequest() throws IOException, InterruptedException {
        // Arrange
        String invalidJson = "{'invalid': 'data'}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(invalidJson))
                .build();

        // Act
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertAll(
                () -> assertEquals(400, response.statusCode(),
                        "При невалидных данных должен возвращаться статус 400"),
                () -> assertTrue(response.body().contains("error"),
                        "Ответ должен содержать сообщение об ошибке"),
                () -> assertTrue(manager.getAllTasks().isEmpty(),
                        "При ошибке не должно добавляться задач")
        );
    }

    @Test
    void testGetTasks_ShouldReturn200Ok() throws IOException, InterruptedException {
        // Arrange
        Task task = new Task("Test", "Description", Status.NEW);
        manager.addTask(task);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasks"))
                .GET()
                .build();

        // Act
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertAll(
                () -> assertEquals(200, response.statusCode()),
                () -> assertTrue(response.body().contains("Test")),
                () -> assertFalse(response.body().isEmpty())
        );
    }

    @Test
    void testGetTaskById_ShouldReturn200Ok() throws IOException, InterruptedException {
        // Arrange
        Task task = new Task("Test", "Description", Status.NEW);
        manager.addTask(task);
        int taskId = task.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasks/" + taskId))
                .GET()
                .build();

        // Act
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertAll(
                () -> assertEquals(200, response.statusCode()),
                () -> assertTrue(response.body().contains("Test")),
                () -> assertTrue(response.body().contains(String.valueOf(taskId)))
        );
    }

    @Test
    void testGetTaskById_WhenNotExists_ShouldReturn404NotFound() throws IOException, InterruptedException {
        // Arrange
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + PORT + "/tasks/999"))
                .GET()
                .build();

        // Act
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert
        assertEquals(404, response.statusCode());
    }
}