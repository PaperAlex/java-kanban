package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import util.LocalDateTimeAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    public EpicsHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Request to /epic");

        String path = exchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        String method = exchange.getRequestMethod();

        if (splitStrings.length == 2) {
            switch (method) {
                case "GET" -> handleGetEpics(exchange);
                case "POST" -> handlePostEpics(exchange);
                case "DELETE" -> handleDeleteEpics(exchange);
                default -> sendNotFound(exchange);
            }
        } else if (splitStrings.length == 3) {
            int epicId = Integer.parseInt(splitStrings[2]);
            switch (method) {
                case "GET" -> handleGetEpicsById(exchange, epicId);
                case "DELETE" -> handleDeleteEpicsById(exchange, epicId);
                default -> sendNotFound(exchange);
            }
        } else {
            System.out.println("Error URL");
            sendNotFound(exchange);
        }

    }

    private void handleGetEpics(HttpExchange exchange) throws IOException {
        System.out.println("Выполняем handleGetEpics: ");
        List<Epic> response = taskManager.getEpicsMap();
        System.out.println(gson.toJson(taskManager.getEpicsMap()));
        if (response.isEmpty()) {
            sendNotFound(exchange);
        }
        sendText(exchange, gson.toJson(response));
    }

    private void handlePostEpics(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        Epic epic = gson.fromJson(jo, Epic.class);
        if (epic.getId() != 0) {
            taskManager.updateEpic(epic);
        } else {
            epic.setSubtasksListIds(new ArrayList<>());
            taskManager.addEpic(epic);
        }
        sendText(exchange, gson.toJson(epic));
    }

    private void handleDeleteEpics(HttpExchange exchange) throws IOException {
        taskManager.deleteEpics();
        sendText(exchange, "Tasks are deleted");
    }

    private void handleGetEpicsById(HttpExchange exchange, int epicId) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        if (splitStrings.length != 3) {
            sendNotFound(exchange);
            return;
        }

        try {
            Epic epic = taskManager.getEpicById(epicId);
            if (epic == null) {
                sendNotFound(exchange);
            } else {
                sendText(exchange, gson.toJson(epic));
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }

    private void handleDeleteEpicsById(HttpExchange exchange, int epicId) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        if (splitStrings.length != 3) {
            sendNotFound(exchange);
            return;
        }
        try {
            Epic epic = taskManager.getEpicById(epicId);
            if (epic == null) {
                sendNotFound(exchange);
            } else {
                taskManager.deleteEpicById(epicId);
                sendText(exchange, " Epic: " + epicId + "are deleted");
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }
}
