package http.handler;

import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;
import model.Subtask;
import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler {
    private static final int MIN_PATH_PARTS = 2;
    private static final int ID_PATH_POSITION = 2;

    public SubtasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            final String method = exchange.getRequestMethod();
            final String path = exchange.getRequestURI().getPath();
            final String[] pathParts = path.split("/");

            switch (method) {
                case "GET":
                    handleGetRequest(exchange, pathParts);
                    break;
                case "POST":
                    handlePostRequest(exchange);
                    break;
                case "DELETE":
                    handleDeleteRequest(exchange, pathParts);
                    break;
                default:
                    sendResponse(exchange, "Метод не поддерживается", 405);
            }
        } catch (NumberFormatException e) {
            sendResponse(exchange, "Некорректный ID", 400);
        } catch (Exception e) {
            sendResponse(exchange, "Внутренняя ошибка сервера", 500);
        } finally {
            exchange.close();
        }
    }

    private void handleGetRequest(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == MIN_PATH_PARTS) {
            // GET /subtasks
            List<Subtask> subtasks = taskManager.getAllSubtasks();
            sendResponse(exchange, gson.toJson(subtasks), 200);
        } else if (pathParts.length == MIN_PATH_PARTS + 1) {
            // GET /subtasks/{id}
            int id = Integer.parseInt(pathParts[ID_PATH_POSITION]);
            Subtask subtask = taskManager.getSubtask(id);
            if (subtask != null) {
                sendResponse(exchange, gson.toJson(subtask), 200);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String requestBody = readText(exchange); // Используем readText из BaseHttpHandler
        Subtask newSubtask = gson.fromJson(requestBody, Subtask.class);

        if (newSubtask == null || newSubtask.getEpicId() <= 0) {
            sendResponse(exchange, "Некорректные данные подзадачи", 400);
            return;
        }

        try {
            taskManager.addSubtask(newSubtask);
            sendResponse(exchange, gson.toJson(newSubtask), 201);
        } catch (Exception e) {
            sendHasInteractions(exchange); // Если есть пересечение по времени
        }
    }

    private void handleDeleteRequest(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == MIN_PATH_PARTS + 1) {
            int id = Integer.parseInt(pathParts[ID_PATH_POSITION]);
            taskManager.deleteSubtask(id);
            sendResponse(exchange, "Подзадача удалена", 200);
        } else {
            sendNotFound(exchange);
        }
    }
}