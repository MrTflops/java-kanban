package http.handler;

import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;
import model.Epic;
import java.io.IOException;

public class EpicsHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
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
                    if (pathParts.length == 3) { // /epics/{id}
                        handleGetEpicById(exchange, pathParts[2]);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "POST":
                    if (pathParts.length == 2) { // /epics
                        handleCreateOrUpdateEpic(exchange);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "DELETE":
                    if (pathParts.length == 3) { // /epics/{id}
                        handleDeleteEpic(exchange, pathParts[2]);
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

    private void handleGetEpicById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Epic epic = taskManager.getEpic(id);
            if (epic == null) {
                sendNotFound(exchange);
            } else {
                String response = gson.toJson(epic);
                sendSuccess(exchange, response);
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handleCreateOrUpdateEpic(HttpExchange exchange) throws IOException {
        Epic epic = readRequest(exchange, Epic.class);
        if (epic == null) {
            sendNotFound(exchange);
            return;
        }

        try {
            if (epic.getId() == 0) { // New epic
                taskManager.addEpic(epic);
                sendCreated(exchange, gson.toJson(epic));
            } else { // Update existing epic
                taskManager.addEpic(epic);
                sendSuccess(exchange, gson.toJson(epic));
            }
        } catch (Exception e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteEpic(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            taskManager.deleteTaskById(id);
            sendSuccess(exchange, "Epic deleted");
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }
}