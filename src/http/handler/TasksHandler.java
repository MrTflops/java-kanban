package http.handler;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;
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
            String[] pathParts = path.split("/");

            switch (method) {
                case "GET":
                    if (pathParts.length == 2) {
                        // GET /tasks
                        List<Task> tasks = taskManager.getAllTasks();
                        sendSuccess(exchange, gson.toJson(tasks));
                    } else if (pathParts.length == 3) {
                        // GET /tasks/{id}
                        int id = Integer.parseInt(pathParts[2]);
                        Task task = taskManager.getTask(id);
                        if (task != null) {
                            sendSuccess(exchange, gson.toJson(task));
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                    break;
                case "POST":
                    // POST /tasks
                    String body = readRequestBody(exchange);
                    Task newTask = parseRequestBody(body, Task.class);
                    taskManager.addTask(newTask);
                    sendCreated(exchange, gson.toJson(newTask));
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        // DELETE /tasks/{id}
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.deleteTaskById(id);
                        sendSuccess(exchange, "Задача удалена");
                    }
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }
}