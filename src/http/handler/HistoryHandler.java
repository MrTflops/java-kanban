package http.handler;

import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = gson.toJson(taskManager.getHistory());
                sendText(exchange, response);
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }
}