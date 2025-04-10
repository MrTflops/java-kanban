package main.http.handler;

import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;
import model.Epic;
import model.Subtask;
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
                        sendResponse(exchange, gson.toJson(epics), 200);
                    } else if (pathParts.length == 3) {
                        if (path.endsWith("/subtasks")) {
                            // GET /epics/{id}/subtasks
                            int epicId = Integer.parseInt(pathParts[2]);
                            List<Subtask> subtasks = taskManager.getEpicSubtasks(epicId);
                            sendResponse(exchange, gson.toJson(subtasks), 200);
                        } else {
                            // GET /epics/{id}
                            int id = Integer.parseInt(pathParts[2]);
                            Epic epic = taskManager.getEpic(id);
                            if (epic != null) {
                                sendResponse(exchange, gson.toJson(epic), 200);
                            } else {
                                sendResponse(exchange, "Эпик не найден", 404);
                            }
                        }
                    }
                    break;
                case "POST":
                    // POST /epics
                    String requestBody = readRequestBody(exchange);
                    Epic newEpic = gson.fromJson(requestBody, Epic.class);
                    taskManager.addEpic(newEpic);
                    sendResponse(exchange, gson.toJson(newEpic), 201);
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        // DELETE /epics/{id}
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.deleteEpic(id);
                        sendResponse(exchange, "Эпик удален", 200);
                    }
                    break;
                default:
                    sendResponse(exchange, "Метод не поддерживается", 405);
            }
        } catch (NumberFormatException e) {
            sendResponse(exchange, "Некорректный ID", 400);
        } catch (Exception e) {
            sendResponse(exchange, "Внутренняя ошибка сервера", 500);
        }
    }
}