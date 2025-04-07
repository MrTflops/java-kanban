package http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import http.*;
import main.TaskManager;
import main.ManagerSaveException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    protected final TaskManager taskManager;
    protected final Gson gson;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = HttpTaskServer.getGson();
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected void sendSuccess(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(201, -1);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(404, -1);
        exchange.close();
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(406, -1);
        exchange.close();
    }

    protected void sendInternalError(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(500, -1);
        exchange.close();
    }

    protected String readText(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    protected String getPathParam(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        return pathParts.length > 2 ? pathParts[2] : null;
    }

    protected void handleException(HttpExchange exchange, Exception e) throws IOException {
        if (e instanceof ManagerSaveException) {
            sendInternalError(exchange);
        } else {
            sendNotFound(exchange);
        }
    }
}