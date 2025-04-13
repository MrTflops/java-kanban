package http.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;
import model.Task;
import util.ManagerSaveException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TasksHandler extends BaseHttpHandler {
    private static final Gson gson = new Gson();
    private static final int ID_PATH_INDEX = 2;

    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            final String method = exchange.getRequestMethod();
            final String path = exchange.getRequestURI().getPath();
            final String[] pathParts = path.split("/");

            switch (method) {
                case "GET":
                    handleGetRequest(exchange, pathParts);
                    break;
                case "POST":
                    handlePostRequest(exchange);
                    break;
                case "DELETE":
                    handleDeleteRequest(exchange, pathParts);
                    break;
                default:
                    sendResponse(exchange, "Method Not Allowed", 405);
            }
        } catch (NumberFormatException e) {
            sendResponse(exchange, "Invalid ID format", 400);
        } catch (Exception e) {
            sendResponse(exchange, "Internal Server Error", 500);
        } finally {
            exchange.close();
        }
    }

    private void handleGetRequest(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 2) {
            List<Task> tasks = taskManager.getAllTasks();
            sendResponse(exchange, gson.toJson(tasks), 200);
        } else if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[ID_PATH_INDEX]);
            Task task = taskManager.getTask(id);
            if (task != null) {
                sendResponse(exchange, gson.toJson(task), 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        try {
            String requestBody = readRequestBody(exchange);
            Task task = gson.fromJson(requestBody, Task.class);

            if (task == null) {
                sendResponse(exchange, "Invalid task data", 400);
                return;
            }

            taskManager.addTask(task);
            sendResponse(exchange, gson.toJson(task), 201);
        } catch (JsonSyntaxException e) {
            sendResponse(exchange, "Invalid JSON format", 400);
        } catch (ManagerSaveException e) {
            sendResponse(exchange, e.getMessage(), 406);
        }
    }

    private void handleDeleteRequest(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[ID_PATH_INDEX]);
            taskManager.deleteTask(id);
            sendResponse(exchange, "", 200);
        } else {
            sendNotFound(exchange);
        }
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private void sendResponse(HttpExchange exchange, String responseText, int statusCode) throws IOException {
        byte[] response = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    private void sendNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, -1);
    }
}