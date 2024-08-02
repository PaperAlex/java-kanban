package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler {

    protected TaskManager taskManager;
    protected Gson gson;

    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        h.sendResponseHeaders(200, text.getBytes().length);
        try (OutputStream os = h.getResponseBody()) {
            os.write(text.getBytes(StandardCharsets.UTF_8));
        }
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        String resp = "Send Not Found";
        h.sendResponseHeaders(404, resp.getBytes().length);
        h.getResponseBody().write(resp.getBytes());
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        String resp = "Send has interactions";
        h.sendResponseHeaders(409, resp.getBytes().length);
        h.getResponseBody().write(resp.getBytes());
    }
}
