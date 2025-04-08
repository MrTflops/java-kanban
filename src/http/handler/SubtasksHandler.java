package http.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;
import model.Subtask;

import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler {
    public SubtasksHandler(TaskManager taskManager) {
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
                        // GET /subtasks
                        List<Subtask> subtasks = taskManager.getAllSubtasks();
                        sendSuccess(exchange, gson.toJson(subtasks));
                    } else if (pathParts.length == 3) {
                        // GET /subtasks/{id}
                        int id = Integer.parseInt(pathParts[2]);
                        Subtask subtask = taskManager.getSubtask(id);
                        if (subtask != null) {
                            sendSuccess(exchange, gson.toJson(subtask));
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                    break;
                case "POST":
                    // POST /subtasks
                    String body = readRequestBody(exchange);
                    Subtask newSubtask = parseRequestBody(body, Subtask.class);
                    taskManager.addSubtask(newSubtask);
                    sendCreated(exchange, gson.toJson(newSubtask));
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        // DELETE /subtasks/{id}
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.deleteTaskById(id);
                        sendSuccess(exchange, "Подзадача удалена");
                    }
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (JsonSyntaxException e) {
            sendNotAcceptable(exchange, "Неверный формат данных");
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }
}