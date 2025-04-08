package test.http.handler;

import main.http.TasksHandler;
import main.TaskManager;
import model.Task;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TasksHandlerTest {
    private TasksHandler tasksHandler;
    private TaskManager taskManager;
    private HttpExchange exchange;
    private Gson gson;

    @BeforeEach
    void setUp() {
        taskManager = mock(TaskManager.class);
        tasksHandler = new TasksHandler(taskManager);
        gson = new Gson();
        exchange = mock(HttpExchange.class);
    }

    @Test
    void handleGetAllTasks() throws IOException {
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getRequestURI()).thenReturn(URI.create("http://localhost:8080/tasks"));

        Task task = new Task("Test", "Description", Status.NEW);
        when(taskManager.getAllTasks()).thenReturn(List.of(task));

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        when(exchange.getResponseBody()).thenReturn(os);

        tasksHandler.handle(exchange);

        verify(exchange).sendResponseHeaders(200, os.toByteArray().length);
        assertTrue(os.toString().contains("Test"));
    }
}