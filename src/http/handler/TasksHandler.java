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
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            switch (method) {
                case "GET":
                    if (path.equals("/tasks")) {
                        List<Task> tasks = taskManager.getAllTasks();
                        sendResponse(exchange, gson.toJson(tasks), 200);
                    } else {
                        sendResponse(exchange, "Not Found", 404);
                    }
                    break;
                case "POST":
                    String requestBody = readRequestBody(exchange);
                    Task task = gson.fromJson(requestBody, Task.class);
                    taskManager.addTask(task);
                    sendResponse(exchange, gson.toJson(task), 201);
                    break;
                default:
                    sendResponse(exchange, "Method Not Allowed", 405);
            }
        } catch (Exception e) {
            sendResponse(exchange, "Internal Server Error", 500);
        }
    }
}