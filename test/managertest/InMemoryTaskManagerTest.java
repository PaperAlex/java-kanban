package managertest;

import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {


    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
    }

    /**
     * Тест, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
     */
    @Test
    void addNewTask() {

        Task task = new Task("Test addNewTask", "Test addNewTask description", 0, Status.NEW);
        taskManager.addTask(task);

        final int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        Assertions.assertNotNull(savedTask, "Задача не найдена.");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasksMap();

        Assertions.assertNotNull(tasks, "Задачи не возвращаются.");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addNewEpic() {
        Subtask subtask1 = new Subtask("Test addNewEpicTest", "addNewEpicTest description",
                0, Status.NEW, 1);
        Subtask subtask2 = new Subtask("Test addNewEpicTest2", "addNewEpicTest description2",
                0, Status.NEW, 1);
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description",
                0, Status.NEW);


        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        final int subtaskId1 = subtask1.getId();
        final int subtaskId2 = subtask2.getId();

        final int epicId = epic.getId();

        final List<Integer> subTaskIds = List.of(subtaskId1, subtaskId2);

        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpicsMap();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertEquals(subTaskIds, savedEpic.getSubtasksListIds(), "Эпик неправильно сохранил подзадачи");
    }

    @Test
    public void addNewSubtask() {
        Epic epic1 = new Epic("Сходить в магазин", "Купить продукты", 1, Status.NEW);
        Subtask subtask1 = new Subtask("Test addNewSubtask", "description", 0, Status.NEW, 1);

        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        final int subtaskId = subtask1.getId();

        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask1, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasksMap();

        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask1, subtasks.get(0), "Задачи не совпадают.");
    }

    /**
     * Тест, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
     */
    @Test
    public void idConflict() {
        Task task = new Task("Test idConflict", "Test idConflict description", 0, Status.NEW);
        taskManager.addTask(task);
        Task task2 = new Task("Test idConflict", "Test idConflict description", 0, Status.NEW);
        taskManager.addTask(task2);

        Assertions.assertNotEquals(task.getId(), task2.getId());
    }

    /**
     * Тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
     */
    @Test
    public void immutabilityOfTask() {
        Task task = new Task("Test immutability", "Test immutability description", 0, Status.NEW);
        taskManager.addTask(task);
        Task task2 = taskManager.getTaskById(task.getId());

        Assertions.assertEquals(task.getId(), task2.getId(), "Поменялся ID");

    }

    /**
     * Тест, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
     */
    @Test
    void taskKeepsInHistoryPreviousVersion() {
        Task task = new Task("Написать код", "Писать код на JAVA", 0, Status.NEW);

        taskManager.addTask(task);
        taskManager.getTaskById(task.getId());
        Task task1 = new Task("Написать код", "Писать код на JAVA быстро", 1, Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        int task1Id = task1.getId();
        Assertions.assertEquals(task1, task);
        Assertions.assertEquals(task1, taskManager.getHistory().getFirst());
        Assertions.assertEquals(task, taskManager.getHistory().getFirst());
    }

    /**
     * С помощью сеттеров экземпляры задач позволяют изменить любое своё поле.
     */
    @Test
    void setNewId() {
        Task task = new Task("Написать код", "Писать код на JAVA", 0, Status.NEW);

        taskManager.addTask(task);
        taskManager.getTaskById(task.getId());
        System.out.println("taskId = " + task.getId());
        Task task1 = new Task("Написать код", "Писать код на JAVA быстро", 1, Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        System.out.println("task1Id = " + task1.getId());
        Assertions.assertEquals(task1, task);

        task1.setId(3);
        System.out.println("task1Id = " + task1.getId());

        Assertions.assertNotEquals(task1, task);

    }

    @Test
    void deleteSubtask() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description",
                0, Status.NEW);
        Subtask subtask1 = new Subtask("Test addNewEpicTest", "addNewEpicTest description",
                0, Status.NEW, 1);
        Subtask subtask2 = new Subtask("Test addNewEpicTest2", "addNewEpicTest description2",
                0, Status.NEW, 1);

        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        taskManager.deleteSubtasks();
        assertEquals(0, taskManager.getSubtasksMap().size(), "SubtasksMap is Not Empty");

    }
}
