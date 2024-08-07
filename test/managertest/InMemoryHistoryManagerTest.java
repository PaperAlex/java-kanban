package managertest;

import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    /**
     * Проверка, что встроенный связный список версий, а также операции добавления и удаления работают корректно.
     */
    @Test
    public void addHistory() {
        Task task = new Task("Test addHistory", "Test addHistory description", 0, Status.NEW,
                1000, LocalDateTime.of(2024, 7, 5, 0, 0));
        taskManager.addTask(task);

        Subtask subtask1 = new Subtask("Test addNewEpicTest", "addNewEpicTest description",
                0, Status.NEW, 2, 1000, LocalDateTime.of(2024, 7, 1, 0, 0));
        Subtask subtask2 = new Subtask("Test addNewEpicTest2", "addNewEpicTest description2",
                0, Status.NEW, 2, 1000, LocalDateTime.of(2024, 7, 2, 0, 0));
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description",
                0, Status.NEW);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        assertEquals(0, taskManager.getHistory().size(), "History is Not Empty");
        taskManager.getEpicById(epic.getId());
        assertEquals(1, taskManager.getHistory().size(), "History is Empty");
    }

    @Test
    void getHistory() {
        Task task = new Task("Test getHistory", "Test getHistory description", 0, Status.NEW,
                1000, LocalDateTime.of(2024, 7, 5, 0, 0));
        taskManager.addTask(task);

        Subtask subtask1 = new Subtask("Test addNewEpicTest", "addNewEpicTest description",
                0, Status.NEW, 2, 1000, LocalDateTime.of(2024, 7, 1, 0, 0));
        Subtask subtask2 = new Subtask("Test addNewEpicTest2", "addNewEpicTest description2",
                0, Status.NEW, 2, 1000, LocalDateTime.of(2024, 7, 2, 0, 0));
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description",
                0, Status.NEW);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getTaskById(task.getId());

        System.out.println(taskManager.getHistory());

        assertEquals(3, taskManager.getHistory().size(), "History is not Empty");
        taskManager.deleteTaskById(task.getId());
        assertEquals(2, taskManager.getHistory().size(), "History is not Empty");
        taskManager.deleteEpicById(epic.getId());
        assertEquals(1, taskManager.getHistory().size(), "History is not Empty");
    }

    @Test
    void deleteHistory() {
        Task task = new Task("Test deleteHistory", "Test deleteHistory description", 0, Status.NEW,
                1000, LocalDateTime.of(2024, 7, 5, 0, 0));
        taskManager.addTask(task);

        Subtask subtask1 = new Subtask("Test addNewEpicTest", "addNewEpicTest description",
                0, Status.NEW, 2, 1000, LocalDateTime.of(2024, 7, 1, 0, 0));
        Subtask subtask2 = new Subtask("Test addNewEpicTest2", "addNewEpicTest description2",
                0, Status.NEW, 2, 1000, LocalDateTime.of(2024, 7, 2, 0, 0));
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description",
                0, Status.NEW);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getTaskById(task.getId());

        assertEquals(3, taskManager.getHistory().size(), "History is not Empty");
        taskManager.deleteSubtasks();
        taskManager.deleteEpics();
        taskManager.deleteTasks();
        assertEquals(0, taskManager.getHistory().size(), "History is Not Empty");

    }

    @Test
    void deleteTasksFormTheMiddle() {
        Task task = new Task("Test deleteHistory", "Test deleteHistory description", 0, Status.NEW,
                1000, LocalDateTime.of(2024, 8, 5, 0, 0));
        taskManager.addTask(task);
        Task task2 = new Task("Test deleteHistory", "Test deleteHistory description", 0, Status.NEW,
                1000, LocalDateTime.of(2024, 7, 5, 0, 0));
        taskManager.addTask(task2);

        Subtask subtask1 = new Subtask("Test addNewEpicTest", "addNewEpicTest description",
                0, Status.NEW, 3, 1000, LocalDateTime.of(2024, 7, 1, 0, 0));
        Subtask subtask2 = new Subtask("Test addNewEpicTest2", "addNewEpicTest description2",
                0, Status.NEW, 3, 1000, LocalDateTime.of(2024, 7, 2, 0, 0));
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description",
                0, Status.NEW);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task2.getId());
        assertEquals(4, taskManager.getHistory().size(), "History is Empty");

        List<Task> taskList = taskManager.getHistory();

        assertEquals(4, taskList.size(), "В истории задач не 3 записи");
        System.out.println(taskManager.getHistory());

        taskManager.deleteTaskById(task2.getId());

        System.out.println(taskManager.getHistory());
        List<Task> taskListNew = taskManager.getHistory();
        Task middleTaskNew = taskListNew.get(1);

        assertNotEquals(task2, middleTaskNew, "Элемент не удален из истории!");
    }

}
