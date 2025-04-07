package http.handler;

import com.sun.net.httpserver.HttpExchange;
import main.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {
    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = gson.toJson(taskManager.getPrioritizedTasks());
                sendText(exchange, response);
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            handleException(exchange, e);
        }
    }
}