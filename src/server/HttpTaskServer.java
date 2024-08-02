package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import util.DurationAdapter;
import util.LocalDateTimeAdapter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private final int port = 8080;
    private final HttpServer httpServer;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/tasks", new TasksHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicsHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager, gson));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));
        httpServer.createContext("/history", new HistoryHandler(taskManager, gson));
    }

    public void start() {
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + port + " порту!");
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Завершили работу сервера на " + port + " порту!");
    }


    public static void main(String[] args) throws IOException {

        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();

        Task task = new Task("Test server1", "Test server description", 0, Status.NEW,
                1000, LocalDateTime.of(2024, 8, 5, 0, 0));
        taskManager.addTask(task);
        Task task2 = new Task("Test server2", "Test server2 description", 0, Status.NEW,
                1000, LocalDateTime.of(2024, 7, 5, 0, 0));
        taskManager.addTask(task2);

        Subtask subtask1 = new Subtask("Test NewEpicTest", "EpicTest description",
                0, Status.NEW, 3, 1000, LocalDateTime.of(2024, 7, 1, 0, 0));
        Subtask subtask2 = new Subtask("Test NewEpicTest2", "EpicTest description2",
                0, Status.NEW, 3, 1000, LocalDateTime.of(2024, 7, 2, 0, 0));
        Epic epic = new Epic("Test serverEpic", "Test serverEpic description",
                0, Status.NEW);


        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        System.out.println("printAllTasks");
        printAllTasks(taskManager);
        System.out.println("---");
    }

    private static void printAllTasks(TaskManager taskManager) {
        System.out.println("Задачи:");
        for (Task task : taskManager.getTasksMap()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : taskManager.getEpicsMap()) {
            System.out.println(epic);

            for (Task task : taskManager.getSubTasksByEpicId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : taskManager.getSubtasksMap()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}
