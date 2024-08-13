package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Request to /subtasks");

        String path = exchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        String method = exchange.getRequestMethod();

        if (splitStrings.length == 2) {
            switch (method) {
                case "GET" -> handleGetSubtasks(exchange);
                case "POST" -> handlePostSubtasks(exchange);
                case "DELETE" -> handleDeleteSubtasks(exchange);
                default -> sendNotFound(exchange);
            }
        } else if (splitStrings.length == 3) {
            int subtaskId = Integer.parseInt(splitStrings[2]);
            switch (method) {
                case "GET" -> handleGetSubtaskById(exchange, subtaskId);
                case "DELETE" -> handleDeleteSubtaskById(exchange, subtaskId);
                default -> sendNotFound(exchange);
            }
        } else {
            System.out.println("Ошибка в URL");
            sendNotFound(exchange);
        }

    }

    private void handleGetSubtasks(HttpExchange exchange) throws IOException {
        System.out.println("Выполняем handleGetSubtasks: ");
        List<Subtask> response = taskManager.getSubtasksMap();
        System.out.println(gson.toJson(taskManager.getSubtasksMap()));
        if (response.isEmpty()) {
            sendNotFound(exchange);
        }
        sendText(exchange, gson.toJson(response));
    }

    private void handlePostSubtasks(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        Subtask subtask = gson.fromJson(jo, Subtask.class);
        if (subtask.getId() != 0) {
            taskManager.updateSubtask(subtask);
        } else {
            taskManager.addSubtask(subtask);
        }
        String response = gson.toJson(subtask);
        sendText(exchange, response);
    }

    private void handleDeleteSubtasks(HttpExchange exchange) throws IOException {
        taskManager.deleteSubtasks();
        sendText(exchange, "Tasks are deleted");
    }

    private void handleGetSubtaskById(HttpExchange exchange, int subtaskId) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        if (splitStrings.length != 3) {
            sendNotFound(exchange);
            return;
        }

        try {
            Subtask subtask = taskManager.getSubtaskById(subtaskId);
            if (subtask == null) {
                sendNotFound(exchange);
            } else {
                sendText(exchange, gson.toJson(subtask));
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handleDeleteSubtaskById(HttpExchange exchange, int subtaskId) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        if (splitStrings.length != 3) {
            sendNotFound(exchange);
            return;
        }
        try {
            Subtask subtask = taskManager.getSubtaskById(subtaskId);
            if (subtask == null) {
                sendNotFound(exchange);
            } else {
                taskManager.deleteSubtaskById(subtaskId);
                sendText(exchange, " Subtask: " + subtaskId + "are deleted");
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }
}
