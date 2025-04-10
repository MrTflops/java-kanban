package http.handler;

import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;
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
                        sendResponse(exchange, gson.toJson(subtasks), 200);
                    } else if (pathParts.length == 3) {
                        // GET /subtasks/{id}
                        int id = Integer.parseInt(pathParts[2]);
                        Subtask subtask = taskManager.getSubtask(id);
                        if (subtask != null) {
                            sendResponse(exchange, gson.toJson(subtask), 200);
                        } else {
                            sendResponse(exchange, "Подзадача не найдена", 404);
                        }
                    }
                    break;
                case "POST":
                    // POST /subtasks
                    String requestBody = readRequestBody(exchange);
                    Subtask newSubtask = gson.fromJson(requestBody, Subtask.class);
                    taskManager.addSubtask(newSubtask);
                    sendResponse(exchange, gson.toJson(newSubtask), 201);
                    break;
                case "DELETE":
                    if (pathParts.length == 3) {
                        // DELETE /subtasks/{id}
                        int id = Integer.parseInt(pathParts[2]);
                        taskManager.deleteSubtask(id);
                        sendResponse(exchange, "Подзадача удалена", 200);
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