package http.handler;

import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;
import model.Subtask;
import java.io.IOException;

public class SubtasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public SubtasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }


    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");

            switch (method) {
                case "GET":
                    if (pathParts.length == 3) { // /subtasks/{id}
                        handleGetSubtaskById(exchange, pathParts[2]);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "POST":
                    if (pathParts.length == 2) { // /subtasks
                        handleCreateOrUpdateSubtask(exchange);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "DELETE":
                    if (pathParts.length == 3) { // /subtasks/{id}
                        handleDeleteSubtask(exchange, pathParts[2]);
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

    private void handleGetSubtaskById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Subtask subtask = taskManager.getSubtask(id);
            if (subtask == null) {
                sendNotFound(exchange);
            } else {
                String response = gson.toJson(subtask);
                sendSuccess(exchange, response);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handleCreateOrUpdateSubtask(HttpExchange exchange) throws IOException {
        Subtask subtask = readRequest(exchange, Subtask.class);
        if (subtask == null) {
            sendNotFound(exchange);
            return;
        }

        try {
            if (subtask.getId() == 0) { // New subtask
                taskManager.addSubtask(subtask);
                sendCreated(exchange, gson.toJson(subtask));
            } else { // Update existing subtask
                taskManager.addSubtask(subtask);
                sendSuccess(exchange, gson.toJson(subtask));
            }
        } catch (Exception e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteSubtask(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            taskManager.deleteTaskById(id);
            sendSuccess(exchange, "Subtask deleted");
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }
}