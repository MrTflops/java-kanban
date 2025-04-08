package main.http.handler;

import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;
import model.Task;
import java.io.IOException;
import java.util.List;

public class TasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");

            switch (method) {
                case "GET":
                    if (pathParts.length == 2) {
                        handleGetAllTasks(exchange);
                    } else if (pathParts.length == 3) {
                        handleGetTaskById(exchange, pathParts[2]);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "POST":
                    if (pathParts.length == 2) {
                        handleCreateOrUpdateTask(exchange);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        handleDeleteTask(exchange, pathParts[2]);
                    } else if (pathParts.length == 2) {
                        handleDeleteAllTasks(exchange);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void handleGetAllTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskManager.getAllTasks();
        String response = gson.toJson(tasks);
        sendSuccess(exchange, response);
    }

    private void handleGetTaskById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Task task = taskManager.getTask(id);
            if (task == null) {
                sendNotFound(exchange);
            } else {
                String response = gson.toJson(task);
                sendSuccess(exchange, response);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handleCreateOrUpdateTask(HttpExchange exchange) throws IOException {
        Task task = readRequest(exchange, Task.class);
        if (task == null) {
            sendNotFound(exchange);
            return;
        }

        try {
            if (task.getId() == 0) {
                taskManager.addTask(task);
                sendCreated(exchange, gson.toJson(task));
            } else {
                taskManager.addTask(task);
                sendSuccess(exchange, gson.toJson(task));
            }
        } catch (Exception e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteTask(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            taskManager.deleteTaskById(id);
            sendSuccess(exchange, "Task deleted");
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handleDeleteAllTasks(HttpExchange exchange) throws IOException {
        taskManager.deleteAllTasks();
        sendSuccess(exchange, "All tasks deleted");
    }
}