package managertest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.Assertions;
import util.LocalDateTimeAdapter;
import util.DurationAdapter;
import model.Status;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {

    TaskManager taskManager = Managers.getDefaultFile();
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    public void beforeEach() {
        taskManager.deleteTasks();
        taskManager.deleteSubtasks();
        taskManager.deleteEpics();

        Task task = new Task("Test task server1", "Test server1 description", 0, Status.NEW,
                1000, LocalDateTime.of(2024, 7, 1, 0, 0));
        taskManager.addTask(task);

        Subtask subtask1 = new Subtask("Test subtask server", "addNewEpicTest description",
                0, Status.NEW, 2,
                1000, LocalDateTime.of(2024, 7, 2, 0, 0));
        Subtask subtask2 = new Subtask("Test subtask2 server", "addNewEpicTest2 description2",
                0, Status.NEW, 2,
                1000, LocalDateTime.of(2024, 7, 3, 0, 0));
        Epic epic = new Epic("Test epic server1", "Test addNewEpic description",
                0, Status.NEW);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        taskServer.start();
    }

    @AfterEach
    public void afterEach() {
        taskServer.stop();
    }

    @Test
    public void pathMistakeTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/test");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Expecting code: 404, get code: " + response.statusCode());
    }

    @Test
    public void getTasksMapTest() throws IOException, InterruptedException {

        for (Task t : taskManager.getTasksMap()) {
            System.out.println(t);
        }

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasksList = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode());
        assertNotNull(tasksList, "No return tasks");
        assertEquals(1, tasksList.size(), "Size don't match");
    }

    @Test
    public void getEpicsMapTest() throws IOException, InterruptedException {
        for (Epic t : taskManager.getEpicsMap()) {
            System.out.println(t);
        }

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type epicType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> epicList = gson.fromJson(response.body(), epicType);

        assertEquals(200, response.statusCode());
        assertNotNull(epicList, "No return tasks");
        assertEquals(1, epicList.size(), "Size don't match");
    }

    @Test
    public void getSubtasksMapTest() throws IOException, InterruptedException {
        for (Subtask t : taskManager.getSubtasksMap()) {
            System.out.println(t);
        }

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type subtaskType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> epicList = gson.fromJson(response.body(), subtaskType);

        assertEquals(200, response.statusCode());
        assertNotNull(epicList, "No return tasks");
        assertEquals(2, epicList.size(), "Size don't match");
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        System.out.println(taskManager.getTaskById(1));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Task task = gson.fromJson(response.body(), Task.class);

        assertEquals(200, response.statusCode());
        assertNotNull(task, "No return tasks");
        assertEquals(taskManager.getTaskById(1), task, "Task don't match");
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        System.out.println(taskManager.getEpicById(2));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/2");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Epic epic = gson.fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode());
        assertNotNull(epic, "No return epics");
        assertEquals(taskManager.getEpicById(2), epic, "Epic don't match");
    }

    @Test
    void getSubTasksById() throws IOException, InterruptedException {
        System.out.println(taskManager.getSubtaskById(3));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/3");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Subtask subtask = gson.fromJson(response.body(), Subtask.class);

        assertEquals(200, response.statusCode());
        assertNotNull(subtask, "No return subtasks");
        assertEquals(taskManager.getSubtaskById(3), subtask, "Subtask don't match");
    }

    @Test
    public void addNewTaskTest() throws IOException, InterruptedException {
        Task taskTest = new Task("TaskAddTest", "description addTaskTest", 0, Status.NEW,
                1200, LocalDateTime.of(2025, 12, 12, 0, 0));
        String json = gson.toJson(taskTest);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        for (Task t : taskManager.getTasksMap()) {
            System.out.println(t);
        }

        assertEquals(200, response.statusCode());
        assertEquals(2, taskManager.getTasksMap().size(), "No return subtasks");
        assertEquals(taskManager.getTaskById(5).getDescription(), "description addTaskTest", "Task don't match");

    }

    @Test
    public void addNewEpicTest() throws IOException, InterruptedException {

        Epic epicTest = new Epic("EpicAddTest", "description addEpicTest", 0, Status.NEW, 1000, LocalDateTime.of(2027, 7, 3, 0, 0));
        String json = gson.toJson(epicTest);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(2, taskManager.getEpicsMap().size(), "No return subtasks");
        assertEquals(taskManager.getEpicById(5).getDescription(), "description addEpicTest", "Epic don't match");

    }

    @Test
    public void addNewSubtaskTest() throws IOException, InterruptedException {
        Subtask subtaskTest = new Subtask("TaskAddTest", "description addSubtaskTest", 0, Status.NEW, 2,
                1200, LocalDateTime.of(2026, 12, 12, 0, 0));
        String json = gson.toJson(subtaskTest);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(body)
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(3, taskManager.getSubtasksMap().size(), "No return subtasks");
        assertEquals(taskManager.getSubtaskById(5).getDescription(), "description addSubtaskTest", "Subtask don't match");

    }

    @Test
    void deleteTaskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Cod response is not a 200");
        Assertions.assertTrue(taskManager.getTasksMap().isEmpty(), "Task are not deleted");
    }

    @Test
    void deleteEpicByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/2");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Cod response is not a 200");
        Assertions.assertTrue(taskManager.getEpicsMap().isEmpty(), "Task are not deleted");
    }

    @Test
    void deleteSubtaskByIdTest() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/3");
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Cod response is not a 200");
        assertEquals(1, taskManager.getSubtasksMap().size());
    }

    @Test
    public void historyTest() throws IOException, InterruptedException {
        taskManager.getTaskById(1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> history = taskManager.getHistory();
        System.out.println(history);

        assertEquals(1, history.size());
        assertEquals("Test server1 description", history.getFirst().getDescription());

    }

    @Test
    void getPrioritizedTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> prioritized = gson.fromJson(response.body(), taskType);

        System.out.println(prioritized);
        assertNotNull(prioritized, "Didn't get priority list");
        assertEquals(3, prioritized.size(), "Number of prioritized tasks is not 3");
        assertEquals(taskManager.getPrioritizedTasks().get(0).getId(), prioritized.get(0).getId(),
                "First task id didn't match");
        assertEquals(taskManager.getPrioritizedTasks().get(1).getId(), prioritized.get(1).getId(),
                "Last task id didn't match");
    }

}
