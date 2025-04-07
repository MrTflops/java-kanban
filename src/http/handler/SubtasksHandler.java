package http.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import model.Subtask;
import main.TaskManager;

import java.io.IOException;

public class SubtasksHandler extends BaseHttpHandler {
    public SubtasksHandler(TaskManager taskManager) {
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
                        String response = gson.toJson(taskManager.getSubtasks());
                        sendText(exchange, response);
                    } else {
                        int id = Integer.parseInt(pathParam);
                        Subtask subtask = taskManager.getSubtask(id);
                        if (subtask != null) {
                            sendText(exchange, gson.toJson(subtask));
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                    break;
                case "POST":
                    String body = readText(exchange);
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    if (subtask.getId() != 0) {
                        taskManager.updateSubtask(subtask);
                    } else {
                        taskManager.addSubtask(subtask);
                    }
                    sendSuccess(exchange);
                    break;
                case "DELETE":
                    if (pathParam != null) {
                        int id = Integer.parseInt(pathParam);
                        taskManager.deleteSubtaskById(id);
                        sendSuccess(exchange);
                    } else {
                        taskManager.deleteAllSubtasks();
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