package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URI;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TasksHandlerTest {
    private TasksHandler handler;
    private StubTaskManager taskManager;
    private Gson gson;

    @BeforeEach
    void setUp() {
        taskManager = new StubTaskManager();
        handler = new TasksHandler(taskManager);
        gson = new Gson();
    }

    @Test
    void testHandleGetTasks() throws IOException {
        Task task = new Task("Test", "Description", Status.NEW);
        taskManager.addTask(task);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HttpExchangeStub exchange = new HttpExchangeStub("GET", new URI("http://localhost:8080/tasks"), os);

        handler.handle(exchange);

        String result = os.toString();
        assertTrue(result.contains("Test"));
        assertEquals(200, exchange.responseCode);
    }

    @Test
    void testHandlePostTask() throws IOException {
        String taskJson = "{\"title\":\"New Task\",\"description\":\"Description\",\"status\":\"NEW\"}";
        InputStream is = new ByteArrayInputStream(taskJson.getBytes());

        HttpExchangeStub exchange = new HttpExchangeStub("POST", new URI("http://localhost:8080/tasks"), new ByteArrayOutputStream(), is);

        handler.handle(exchange);

        assertEquals(1, taskManager.tasks.size());
    }

    @Test
    void testHandleDeleteTask() throws IOException {
        Task task = new Task("ToDelete", "Description", Status.NEW);
        taskManager.addTask(task);

        HttpExchangeStub exchange = new HttpExchangeStub("DELETE", new URI("http://localhost:8080/tasks/" + task.getId()), new ByteArrayOutputStream());

        handler.handle(exchange);

        assertTrue(taskManager.deletedIds.contains(task.getId()));
    }

    // Заглушка TaskManager
    static class StubTaskManager implements TaskManager {
        List<Task> tasks = new ArrayList<>();
        Set<Integer> deletedIds = new HashSet<>();
        private int nextId = 1;

        @Override
        public List<Task> getAllTasks() {
            return tasks;
        }

        @Override
        public void addTask(Task task) {
            task.setId(nextId++);
            tasks.add(task);
        }

        @Override
        public void deleteTask(int id) {
            deletedIds.add(id);
        }

        // --- Остальные методы интерфейса не используются в тестах ---
        public Task getTask(int id) { return null; }
        public void updateTask(Task task) {}
        public void deleteAllTasks() {}
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
        public List<Task> getPrioritizedTasks() { return null; }
    }

    // Заглушка HttpExchange
    static class HttpExchangeStub extends HttpExchange {
        private final String method;
        private final URI uri;
        private final OutputStream os;
        private final InputStream is;
        int responseCode;

        public HttpExchangeStub(String method, URI uri, OutputStream os) {
            this(method, uri, os, new ByteArrayInputStream(new byte[0]));
        }

        public HttpExchangeStub(String method, URI uri, OutputStream os, InputStream is) {
            this.method = method;
            this.uri = uri;
            this.os = os;
            this.is = is;
        }

        @Override public Headers getRequestHeaders() { return null; }
        @Override public Headers getResponseHeaders() { return null; }
        @Override public URI getRequestURI() { return uri; }
        @Override public String getRequestMethod() { return method; }
        @Override public HttpContext getHttpContext() { return null; }
        @Override public void close() {}
        @Override public InputStream getRequestBody() { return is; }
        @Override public OutputStream getResponseBody() { return os; }
        @Override public void sendResponseHeaders(int rCode, long responseLength) throws IOException {
            this.responseCode = rCode;
        }
        @Override public InetSocketAddress getRemoteAddress() { return null; }
        @Override public InetSocketAddress getLocalAddress() { return null; }
        @Override public String getProtocol() { return null; }
        @Override public Object getAttribute(String name) { return null; }
        @Override public void setAttribute(String name, Object value) {}
        @Override public void setStreams(InputStream i, OutputStream o) {}
    }
}
