package http.handler;

import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;
import model.Task;
import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                // GET /history
                List<Task> history = taskManager.getHistory();
                sendResponse(exchange, gson.toJson(history), 200);
            } else {
                sendResponse(exchange, "Метод не поддерживается", 405);
            }
        } catch (Exception e) {
            sendResponse(exchange, "Внутренняя ошибка сервера", 500);
        }
    }
}