package http.handler;

import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;
import model.Task;
import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {
    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                // GET /prioritized
                List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                sendResponse(exchange, gson.toJson(prioritizedTasks), 200);
            } else {
                sendResponse(exchange, "Метод не поддерживается", 405);
            }
        } catch (Exception e) {
            sendResponse(exchange, "Внутренняя ошибка сервера", 500);
        }
    }
}