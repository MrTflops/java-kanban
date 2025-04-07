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
                    if (pathParam == null) {
                        String response = gson.toJson(taskManager.getTasks());
                        sendText(exchange, response);
                    } else {
                        int id = Integer.parseInt(pathParam);
                        Task task = taskManager.getTask(id);
                        if (task != null) {
                            sendText(exchange, gson.toJson(task));
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                    break;
                case "POST":
                    String body = readText(exchange);
                    Task task = gson.fromJson(body, Task.class);
                    if (task.getId() != 0) {
                        taskManager.updateTask(task);
                    } else {
                        taskManager.addTask(task);
                    }
                    sendSuccess(exchange);
                    break;
                case "DELETE":
                    if (pathParam != null) {
                        int id = Integer.parseInt(pathParam);
                        taskManager.deleteTaskById(id);
                        sendSuccess(exchange);
                    } else {
                        taskManager.deleteAllTasks();
                        sendSuccess(exchange);
                    }
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (JsonSyntaxException | NumberFormatException e) {
            sendNotFound(exchange);
        } catch (ManagerSaveException e) {
            sendHasInteractions(exchange);
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }
}