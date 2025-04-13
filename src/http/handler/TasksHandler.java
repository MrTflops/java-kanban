package http.handler;

import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;

public class TasksHandler extends BaseHttpHandler {

    public TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            final String method = exchange.getRequestMethod();
            final String path = exchange.getRequestURI().getPath();

            switch (method) {
                case "GET":
                    handleGetTasks(exchange);
                    break;
                case "POST":
                    handlePostTask(exchange);
                    break;
                case "DELETE":
                    handleDeleteTask(exchange, path);
                    break;
                default:
                    sendResponse(exchange, "Method Not Allowed", 405);
            }
        } catch (Exception e) {
            sendResponse(exchange, "Internal Server Error", 500);
        } finally {
            exchange.close();
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        List<Task> tasks = taskManager.getAllTasks();
        sendText(exchange, gson.toJson(tasks));
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        String body = readText(exchange);
        Task task = gson.fromJson(body, Task.class);

        if (task.getId() == 0) {
            taskManager.addTask(task);
            sendResponse(exchange, gson.toJson(task), 201);
        } else {
            taskManager.updateTask(task);
            sendResponse(exchange, gson.toJson(task), 200);
        }
    }

    private void handleDeleteTask(HttpExchange exchange, String path) throws IOException {
        Integer id = getIdFromPath(path);
        if (id == null || id == 0) {
            sendNotFound(exchange);
            return;
        }

        taskManager.deleteTask(id);
        sendResponse(exchange, "", 200);
    }
}