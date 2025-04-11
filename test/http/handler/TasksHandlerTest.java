package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TasksHandlerTest {
    private TasksHandler handler;
    private TaskManager taskManager;
    private HttpExchange exchange;
    private Gson gson;

    @BeforeEach
    void setUp() throws Exception {
        taskManager = mock(TaskManager.class);
        handler = new TasksHandler(taskManager);
        gson = new Gson();
        exchange = mock(HttpExchange.class);
    }

    @Test
    void testHandleGetTasks() throws IOException {
        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getRequestURI()).thenReturn(new URI("http://localhost:8080/tasks"));

        Task task = new Task("Test", "Description", Status.NEW);
        when(taskManager.getAllTasks()).thenReturn(List.of(task));

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        when(exchange.getResponseBody()).thenReturn(os);

        handler.handle(exchange);

        verify(exchange).sendResponseHeaders(200, os.toByteArray().length);
        assertTrue(os.toString().contains("Test"));
    }

    @Test
    void testHandlePostTask() throws IOException {
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestURI()).thenReturn(new URI("http://localhost:8080/tasks"));

        String taskJson = "{\"title\":\"New Task\",\"description\":\"Description\",\"status\":\"NEW\"}";
        InputStream is = new ByteArrayInputStream(taskJson.getBytes());
        when(exchange.getRequestBody()).thenReturn(is);

        handler.handle(exchange);

        verify(taskManager).addTask(any(Task.class));
    }

    @Test
    void testHandleDeleteTask() throws IOException {
        when(exchange.getRequestMethod()).thenReturn("DELETE");
        when(exchange.getRequestURI()).thenReturn(new URI("http://localhost:8080/tasks/1"));

        handler.handle(exchange);

        verify(taskManager).deleteTask(1);
    }
}