package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<Task> prioritized = taskManager.getPrioritizedTasks();
        if (prioritized.isEmpty()) {
            sendNotFound(exchange);
        } else {
            sendText(exchange, gson.toJson(prioritized));
        }
    }
}
