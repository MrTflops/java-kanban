package http.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import main.*;
import model.Task;

import java.io.IOException;

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
                        String response = gson.toJson(taskManager.getTasks());
                        sendText(exchange, response);
                    } else if (pathParts.length == 3) {
                        Integer id = parseId(pathParts[2]);
                        if (id != null) {
                            Task task = taskManager.getTask(id);
                            if (task != null) {
                                sendText(exchange, gson.toJson(task));
                            } else {
                                sendNotFound(exchange);
                            }
                        } else {
                            sendNotFound(exchange);
                        }
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "POST":
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
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        Integer id = parseId(pathParts[2]);
                        if (id != null) {
                            taskManager.deleteTaskById(id);
                            sendSuccess(exchange);
                        } else {
                            sendNotFound(exchange);
                        }
                    } else {
                        taskManager.deleteAllTasks();
                        sendSuccess(exchange);
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