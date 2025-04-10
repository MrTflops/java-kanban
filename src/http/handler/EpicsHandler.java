package http.handler;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;
import model.Epic;

import java.io.IOException;
import java.util.List;

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
                        // GET /epics
                        List<Epic> epics = taskManager.getAllEpics();
                        sendSuccess(exchange, gson.toJson(epics));
                    } else if (pathParts.length == 3) {
                        // GET /epics/{id}
                        int id = Integer.parseInt(pathParts[2]);
                        Epic epic = taskManager.getEpic(id);
                        if (epic != null) {
                            sendSuccess(exchange, gson.toJson(epic));
                        } else {
                            sendNotFound(exchange);
                        }
                    }
                    break;
                case "POST":
                    // POST /epics
                    String body = readRequestBody(exchange);
                    Epic newEpic = parseRequestBody(body, Epic.class);
                    taskManager.addEpic(newEpic);
                    sendCreated(exchange, gson.toJson(newEpic));
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        // DELETE /epics/{id}
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.deleteTaskById(id);
                        sendSuccess(exchange, "Эпик удален");
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