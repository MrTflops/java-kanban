package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

abstract class BaseHttpHandler implements HttpHandler {
    protected final TaskManager taskManager;
    protected final Gson gson;
    protected static final int NUM_PARTS_IN_PATH_WITH_ID = 3;

    protected BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new Gson();
    }

    protected String readText(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        sendResponse(exchange, text, 200);
    }

    protected void sendResponse(HttpExchange exchange, String text, int code) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(code, response.length);
        exchange.getResponseBody().write(response);
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, -1);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        String response = "Есть пересечение с текущими задачами";
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(406, bytes.length);
        exchange.getResponseBody().write(bytes);
    }

    protected Integer getIdFromPath(String path) {
        String[] parts = path.split("/");
        try {
            if (parts.length >= NUM_PARTS_IN_PATH_WITH_ID) {
                return Integer.parseInt(parts[NUM_PARTS_IN_PATH_WITH_ID - 1]);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }
}