package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import main.TaskManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TasksHandlerTest {
    private TasksHandler handler;
    private TaskManagerStub taskManager;
    private Gson gson;

    @BeforeEach
    void setUp() {
        taskManager = new TaskManagerStub();
        handler = new TasksHandler(taskManager);
        gson = new Gson();
    }

    @Test
    void testHandleGetTasks() throws IOException {
        Task task = new Task("Test", "Description", Status.NEW);
        task.setId(1);
        taskManager.addTask(task);

        ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
        HttpExchangeStub exchange = new HttpExchangeStub("GET", "/tasks", null, responseBody);

        handler.handle(exchange);

        String response = responseBody.toString();
        assertTrue(response.contains("Test"));
        assertEquals(200, exchange.getResponseCode());
    }

    @Test
    void testHandlePostTask() throws IOException {
        String taskJson = "{\"title\":\"New Task\",\"description\":\"Description\",\"status\":\"NEW\"}";
        ByteArrayInputStream requestBody = new ByteArrayInputStream(taskJson.getBytes());
        ByteArrayOutputStream responseBody = new ByteArrayOutputStream();

        HttpExchangeStub exchange = new HttpExchangeStub("POST", "/tasks", requestBody, responseBody);

        handler.handle(exchange);

        assertEquals(1, taskManager.tasks.size());
        assertEquals(200, exchange.getResponseCode());
    }

    @Test
    void testHandleDeleteTask() throws IOException {
        Task task = new Task("ToDelete", "Description", Status.NEW);
        task.setId(1);
        taskManager.addTask(task);

        ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
        HttpExchangeStub exchange = new HttpExchangeStub("DELETE", "/tasks/1", null, responseBody);

        handler.handle(exchange);

        assertTrue(taskManager.deletedTaskIds.contains(1));
        assertEquals(200, exchange.getResponseCode());
    }

    // Stub for TaskManager
    static class TaskManagerStub implements TaskManager {
        List<Task> tasks = new ArrayList<>();
        List<Integer> deletedTaskIds = new ArrayList<>();

        @Override
        public List<Task> getAllTasks() {
            return tasks;
        }

        @Override
        public Task getTask(int id) {
            return tasks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
        }

        @Override
        public void addTask(Task task) {
            task.setId(tasks.size() + 1);
            tasks.add(task);
        }

        @Override
        public void updateTask(Task task) {}

        @Override
        public void deleteTask(int id) {
            deletedTaskIds.add(id);
        }

        @Override
        public void deleteAllTasks() {}

        // Остальные методы можно оставить пустыми
        public List<model.Subtask> getAllSubtasks() { return null; }

        public model.Subtask getSubtask(int id) { return null; }

        public void addSubtask(model.Subtask subtask) {}

        public void updateSubtask(model.Subtask subtask) {}

        public void deleteSubtask(int id) {}

        public void deleteAllSubtasks() {}

        public List<model.Subtask> getSubtaskListByEpicId(int epicId) { return null; }

        public List<model.Subtask> getEpicSubtasks(int epicId) { return null; }

        public List<model.Epic> getEpicList() { return null; }

        public List<model.Epic> getAllEpics() { return null; }

        public model.Epic getEpic(int id) { return null; }

        public void addEpic(model.Epic epic) {}

        public void updateEpic(model.Epic epic) {}

        public void deleteEpic(int id) {}

        public void deleteEpicById(int id) {}

        public void deleteAllEpics() {}

        public List<Task> getHistory() { return null; }

        public List<Task> getPrioritizedTasks() { return tasks; }
    }

    // Stub for HttpExchange
    static class HttpExchangeStub extends HttpExchange {
        private final String method;
        private final URI uri;
        private final InputStream requestBody;
        private final OutputStream responseBody;
        private int responseCode;

        HttpExchangeStub(String method, String path, InputStream requestBody, OutputStream responseBody) {
            this.method = method;
            this.uri = URI.create("http://localhost:8080" + path);
            this.requestBody = requestBody != null ? requestBody : new ByteArrayInputStream(new byte[0]);
            this.responseBody = responseBody;
        }

        @Override
        public Headers getRequestHeaders() { return new Headers(); }

        @Override
        public Headers getResponseHeaders() { return new Headers(); }

        @Override
        public URI getRequestURI() { return uri; }

        @Override
        public String getRequestMethod() { return method; }

        @Override
        public HttpContext getHttpContext() { return null; }

        @Override
        public void close() {}

        @Override
        public InputStream getRequestBody() { return requestBody; }

        @Override
        public OutputStream getResponseBody() { return responseBody; }

        @Override
        public void sendResponseHeaders(int rCode, long responseLength) {
            this.responseCode = rCode;
        }

        @Override
        public InetSocketAddress getRemoteAddress() { return null; }

        @Override
        public InetSocketAddress getLocalAddress() { return null; }

        @Override
        public String getProtocol() { return null; }

        @Override
        public Object getAttribute(String name) { return null; }

        @Override
        public void setAttribute(String name, Object value) {}

        @Override
        public void setStreams(InputStream i, OutputStream o) {}

        @Override
        public HttpPrincipal getPrincipal() {
            return new HttpPrincipal("user", "realm");
        }

        public int getResponseCode() {
            return responseCode;
        }
    }
}