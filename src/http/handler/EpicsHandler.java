package http.handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.Subtask;
import service.NotFoundException;
import main.TaskManager;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final int id = getIdFromPath(exchange.getRequestURI().getPath());
        final String response;

        switch (exchange.getRequestMethod()) {
            case "GET":
                if (id == 0) {
                    final List<Epic> epics = taskManager.getEpicList();
                    response = gson.toJson(epics);
                    System.out.println("Получили все эпики");
                    sendText(exchange, response);
                } else {
                    if (exchange.getRequestURI().getPath().endsWith("/subtasks")) {
                        try {
                            final List<Subtask> subtasks = taskManager.getSubtaskListByEpicId(id);
                            response = gson.toJson(subtasks);
                            System.out.println("Получили все подзадачи");
                            sendText(exchange, response);
                        } catch (NotFoundException e) {
                            System.out.println(e.getMessage());
                            sendNotFound(exchange);
                        }
                    } else {
                        try {
                            Epic epic = taskManager.getEpic(id);
                            response = gson.toJson(epic);
                            System.out.println("Получили задачу id = " + id);
                            sendText(exchange, response);

                        } catch (NotFoundException e) {
                            System.out.println(e.getMessage());
                            sendNotFound(exchange);
                        }
                    }
                }
                break;
            case "POST":
                String json = readText(exchange);
                if (json == null || json.isEmpty()) {
                    sendNotFound(exchange);
                    break;
                } else {
                    JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
                    String nameTask = jsonObject.get("nameTask").getAsString();
                    String descriptionTask = jsonObject.get("descriptionTask").getAsString();
                    Epic epic = new Epic(nameTask, descriptionTask);
                    taskManager.addEpic(epic);
                    int id1 = epic.getId();
                    System.out.println("Создали эпик id = " + id1);
                    exchange.sendResponseHeaders(201, 0);
                    exchange.close();
                    break;
                }
            case "DELETE":
                try {
                    taskManager.deleteEpicById(id);
                    System.out.println("Удалили задачу id = " + id);
                    exchange.sendResponseHeaders(200, 0);
                    exchange.close();
                } catch (NotFoundException e) {
                    System.out.println(e.getMessage());
                    sendNotFound(exchange);
                }
                break;
            default:
                sendNotFound(exchange);
        }
    }
}