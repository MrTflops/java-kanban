package http.handler;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;
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
                sendSuccess(exchange, gson.toJson(prioritizedTasks));
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }
}