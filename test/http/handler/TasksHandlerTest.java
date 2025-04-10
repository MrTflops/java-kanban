package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.InMemoryTaskManager;
import main.TaskManager;
import model.Task;
import model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TasksHandlerTest {
    private TasksHandler handler;
    private TaskManager taskManager;
    private Gson gson;
    private HttpExchange exchange;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        handler = new TasksHandler(taskManager);
        gson = HttpTaskServer.getGson();
        exchange = mock(HttpExchange.class);
    }

    @Test
    void testHandleGetTasks() throws IOException, URISyntaxException {
        Task task = new Task("Test", "Description", Status.NEW);
        taskManager.addTask(task);

        when(exchange.getRequestMethod()).thenReturn("GET");
        when(exchange.getRequestURI()).thenReturn(new URI("http://localhost:8080/tasks"));

        OutputStream os = mock(OutputStream.class);
        when(exchange.getResponseBody()).thenReturn(os);

        handler.handle(exchange);

        verify(exchange).sendResponseHeaders(200, gson.toJson(taskManager.getTasks()).getBytes().length);
    }

    @Test
    void testHandlePostTask() throws IOException {
        Task task = new Task("New Task", "Description", Status.NEW);
        String taskJson = gson.toJson(task);

        InputStream is = new ByteArrayInputStream(taskJson.getBytes(StandardCharsets.UTF_8));
        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestBody()).thenReturn(is);

        handler.handle(exchange);

        assertEquals(1, taskManager.getTasks().size());
        assertEquals("New Task", taskManager.getTasks().get(0).getTitle());
    }

    @Test
    void testHandleDeleteTask() throws IOException, URISyntaxException {
        Task task = new Task("To Delete", "Description", Status.NEW);
        taskManager.addTask(task);

        when(exchange.getRequestMethod()).thenReturn("DELETE");
        when(exchange.getRequestURI()).thenReturn(new URI("http://localhost:8080/tasks/1"));

        handler.handle(exchange);

        assertEquals(0, taskManager.getTasks().size());
    }
}