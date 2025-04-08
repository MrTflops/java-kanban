package test.http;

import main.http.HttpTaskServer;
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

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private HttpTaskServer server;
    private TaskManager taskManager;
    private HttpClient client;

    @BeforeEach
    void setUp() throws IOException {
        taskManager = Managers.getDefault();
        server = new HttpTaskServer(taskManager);
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void testAddAndGetTask() throws IOException, InterruptedException {
        Task task = new Task("Test task", "Test description", Status.NEW);
        String taskJson = BaseHttpHandler.gson.toJson(task);

        // Добавляем задачу
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, postResponse.statusCode());

        // Получаем задачу
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getResponse.statusCode());

        Task receivedTask = BaseHttpHandler.gson.fromJson(getResponse.body(), Task.class);
        assertEquals("Test task", receivedTask.getTitle());
        assertEquals("Test description", receivedTask.getDescription());
        assertEquals(Status.NEW, receivedTask.getStatus());
    }

    @Test
    void testGetAllTasks() throws IOException, InterruptedException {
        // Добавляем несколько задач
        taskManager.addTask(new Task("Task 1", "Desc 1", Status.NEW));
        taskManager.addTask(new Task("Task 2", "Desc 2", Status.IN_PROGRESS));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task[] tasks = BaseHttpHandler.gson.fromJson(response.body(), Task[].class);
        assertEquals(2, tasks.length);
    }

    @Test
    void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("To delete", "Desc", Status.NEW);
        taskManager.addTask(task);
        int taskId = task.getId();

        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + taskId))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, deleteResponse.statusCode());

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + taskId))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, getResponse.statusCode());
    }

    @Test
    void testGetHistory() throws IOException, InterruptedException {
        Task task1 = new Task("Task 1", "Desc 1", Status.NEW);
        Task task2 = new Task("Task 2", "Desc 2", Status.IN_PROGRESS);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        // Получаем задачи, чтобы добавить в историю
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task[] history = BaseHttpHandler.gson.fromJson(response.body(), Task[].class);
        assertEquals(2, history.length);
    }
}