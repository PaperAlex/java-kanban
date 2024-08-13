package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import model.Task;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    public TasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Request to /tasks");

        String path = exchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        String method = exchange.getRequestMethod();

        if (splitStrings.length == 2) {
            switch (method) {
                case "GET" -> handleGetTasks(exchange);
                case "POST" -> handlePostTasks(exchange);
                case "DELETE" -> handleDeleteTasks(exchange);
                default -> sendNotFound(exchange);
            }
        } else if (splitStrings.length == 3) {
            int taskId = Integer.parseInt(splitStrings[2]);
            switch (method) {
                case "GET" -> handleGetTaskById(exchange, taskId);
                case "DELETE" -> handleDeleteTaskById(exchange, taskId);
                default -> sendNotFound(exchange);
            }
        } else {
            System.out.println("Ошибка в URL");
            sendNotFound(exchange);
        }

    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        System.out.println("handleGetTasks: ");
        List<Task> response = taskManager.getTasksMap();
        System.out.println(gson.toJson(taskManager.getTasksMap()));
        if (response.isEmpty()) {
            sendNotFound(exchange);
        }
        sendText(exchange, gson.toJson(response));
    }

    private void handlePostTasks(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        JsonObject jo = JsonParser.parseString(body).getAsJsonObject();
        Task task = gson.fromJson(jo, Task.class);
        if (task.getId() != 0) {
            taskManager.updateTask(task);
        } else {
            taskManager.addTask(task);
        }
        String response = gson.toJson(task);
        sendText(exchange, response);
    }

    private void handleDeleteTasks(HttpExchange exchange) throws IOException {
        taskManager.deleteTasks();
        sendText(exchange, "Tasks are deleted");
    }

    private void handleGetTaskById(HttpExchange exchange, int taskId) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        if (splitStrings.length != 3) {
            sendNotFound(exchange);
            return;
        }

        try {
            Task task = taskManager.getTaskById(taskId);
            if (task == null) {
                sendNotFound(exchange);
            } else {
                sendText(exchange, gson.toJson(task));
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
        System.out.println(gson.toJson(taskManager.getTaskById(taskId)));
    }

    private void handleDeleteTaskById(HttpExchange exchange, int taskId) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        if (splitStrings.length != 3) {
            sendNotFound(exchange);
            return;
        }
        try {
            Task task = taskManager.getTaskById(taskId);
            if (task == null) {
                sendNotFound(exchange);
            } else {
                taskManager.deleteTaskById(taskId);
                sendText(exchange, " Task: " + taskId + "are deleted");
            }
        } catch (NumberFormatException e) {
            sendNotFound(exchange);
        }
    }
}
