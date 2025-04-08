package http.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import main.TaskManager;

import java.io.IOException;

public class TasksHandler extends BaseHttpHandler {
    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String pathParam = getPathParam(exchange);

            switch (method) {
                case "GET":
                    handleGet(exchange, pathParam);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange, pathParam);
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }

    private void handleGet(HttpExchange exchange, String pathParam) throws IOException {
        if (pathParam == null) {
            String response = gson.toJson(taskManager.getTasks());
            sendText(exchange, response);
        } else {
            try {
                int id = Integer.parseInt(pathParam);
                Task task = taskManager.getTask(id);
                if (task != null) {
                    sendText(exchange, gson.toJson(task));
                } else {
                    sendNotFound(exchange);
                }
            } catch (NumberFormatException e) {
                sendNotFound(exchange);
            }
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        String body = readText(exchange);
        try {
            Task task = gson.fromJson(body, Task.class);
            if (task.getId() != 0) {
                taskManager.updateTask(task);
            } else {
                taskManager.addTask(task);
            }
            sendSuccess(exchange);
        } catch (JsonSyntaxException e) {
            sendNotFound(exchange);
        } catch (ManagerSaveException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDelete(HttpExchange exchange, String pathParam) throws IOException {
        if (pathParam != null) {
            try {
                int id = Integer.parseInt(pathParam);
                taskManager.deleteTaskById(id);
                sendSuccess(exchange);
            } catch (NumberFormatException e) {
                sendNotFound(exchange);
            }
        } else {
            taskManager.deleteAllTasks();
            sendSuccess(exchange);
        }
    }
}