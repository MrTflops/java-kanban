package http;

import com.google.gson.Gson;
import main.HttpTaskServer;
import main.TaskManager;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HttpTaskServerTest {
    private HttpTaskServer server;
    private TaskManager taskManager;
    private HttpClient client;
    private final Gson gson = new Gson();

    @BeforeEach
    void setUp() throws IOException {
        taskManager = mock(TaskManager.class);
        server = new HttpTaskServer(taskManager);
        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void testAddTask() throws IOException, InterruptedException {
        // Подготовка тестовых данных
        Task testTask = new Task("Test task", "Description", Task.Status.NEW);
        String taskJson = gson.toJson(testTask);

        // Настройка mock-объекта
        when(taskManager.addTask(any(Task.class))).thenAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            t.setId(1); // Устанавливаем ID для имитации сохранения
            return null;
        });

        // Отправка запроса
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверки
        assertEquals(201, response.statusCode(), "Статус код должен быть 201 (Created)");
        verify(taskManager, times(1)).addTask(any(Task.class));

        // Проверка тела ответа
        Task responseTask = gson.fromJson(response.body(), Task.class);
        assertNotNull(responseTask.getId(), "Задача должна иметь ID после сохранения");
    }
}