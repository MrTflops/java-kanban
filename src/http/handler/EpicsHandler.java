package http.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import main.TaskManager;

import java.io.IOException;

public class EpicsHandler extends BaseHttpHandler {
    public EpicsHandler(TaskManager taskManager) {
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
                        String response = gson.toJson(taskManager.getEpics());
                        sendText(exchange, response);
                    } else if (pathParts.length == 3) {
                        int id = Integer.parseInt(pathParts[2]);
                        Epic epic = taskManager.getEpic(id);
                        if (epic != null) {
                            sendText(exchange, gson.toJson(epic));
                        } else {
                            sendNotFound(exchange);
                        }
                    } else if (pathParts.length == 4 && "subtasks".equals(pathParts[3])) {
                        int id = Integer.parseInt(pathParts[2]);
                        String subtasksResponse = gson.toJson(taskManager.getEpicSubtasks(id));
                        sendText(exchange, subtasksResponse);
                    } else {
                        sendNotFound(exchange);
                    }
                    break;
                case "POST":
                    String body = readText(exchange);
                    Epic epic = gson.fromJson(body, Epic.class);
                    taskManager.addEpic(epic);
                    sendSuccess(exchange);
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.deleteEpicById(id);
                        sendSuccess(exchange);
                    } else {
                        taskManager.deleteAllEpics();
                        sendSuccess(exchange);
                    }
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (JsonSyntaxException | NumberFormatException e) {
            sendNotFound(exchange);
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }
}