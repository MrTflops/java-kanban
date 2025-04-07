package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import http.adapter.*;
import main.TaskManager;
import main.Managers;
import http.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private final TaskManager taskManager;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/subtasks", new SubtasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void start() {
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту.");
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP-сервер остановлен.");
    }

    public static Gson getGson() {
        return gson;
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer();
        server.start();
    }
}