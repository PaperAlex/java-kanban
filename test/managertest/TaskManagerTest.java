package managertest;

import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;

    void makeTasks() {
        Task task = new Task("Test addHistory", "Test addHistory description", 0, Status.NEW,
                1000, LocalDateTime.of(2024, 7, 1, 0, 0));
        taskManager.addTask(task);

        Subtask subtask1 = new Subtask("Test addNewEpicTest", "addNewEpicTest description",
                0, Status.NEW, 2,
                1000, LocalDateTime.of(2024, 7, 2, 0, 0));
        Subtask subtask2 = new Subtask("Test addNewEpicTest2", "addNewEpicTest2 description2",
                0, Status.NEW, 2,
                1000, LocalDateTime.of(2024, 7, 3, 0, 0));
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description",
                0, Status.NEW);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
    }

    /**
     * Тест, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
     */
    @Test
    void addNewTask() {

        Task task = taskManager.getTaskById(1);
        final Task savedTask = taskManager.getTaskById(1);

        Assertions.assertNotNull(savedTask, "Задача не найдена.");
        Assertions.assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasksMap();

        Assertions.assertNotNull(tasks, "Задачи не возвращаются.");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач.");
        Assertions.assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addNewEpic() {

        Epic epic = taskManager.getEpicById(2);
        final int subtaskId1 = taskManager.getSubtaskById(3).getId();
        final int subtaskId2 = taskManager.getSubtaskById(4).getId();

        final int epicId = taskManager.getEpicById(2).getId();

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

        Subtask subtask1 = taskManager.getSubtaskById(3);
        final int subtaskId = subtask1.getId();

        final Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask1, savedSubtask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubtasksMap();

        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(2, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask1, subtasks.get(0), "Задачи не совпадают.");
    }

    /**
     * Тест, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
     */
    @Test
    public void idConflict() {
        Task task = taskManager.getTaskById(1);
        Task task2 = new Task("Test addHistory", "Test addHistory description",
                0, Status.NEW, 1000, LocalDateTime.of(2024, 12, 1, 0, 0));
        taskManager.addTask(task2);
        Assertions.assertNotEquals(task.getId(), task2.getId(), "Не может быть одинаковый ID");
    }

    /**
     * Тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
     */
    @Test
    public void immutabilityOfTask() {
        Task task = taskManager.getTaskById(1);
        Task task2 = taskManager.getTaskById(task.getId());

        Assertions.assertEquals(task.getId(), task2.getId(), "Поменялся ID");

    }

    /**
     * Тест, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
     */
    @Test
    void taskKeepsInHistoryPreviousVersion() {
        Task task = taskManager.getTaskById(1);
        taskManager.getTaskById(task.getId());
        Task task1 = new Task("Написать код", "Писать код на JAVA быстро", 1, Status.IN_PROGRESS,
                1000, LocalDateTime.of(2024, 12, 1, 0, 0));
        taskManager.updateTask(task1);

        Assertions.assertEquals(task1, task);
        Assertions.assertEquals(task1, taskManager.getHistory().getFirst());
        Assertions.assertEquals(task, taskManager.getHistory().getFirst());
    }

    /**
     * С помощью сеттеров экземпляры задач позволяют изменить любое своё поле.
     */
    @Test
    void setNewId() {
        Task task = taskManager.getTaskById(1);
        taskManager.getTaskById(task.getId());
        System.out.println("taskId = " + task.getId());
        Task task1 = new Task("Написать код", "Писать код на JAVA быстро", 1, Status.IN_PROGRESS,
                1000, LocalDateTime.of(2025, 1, 1, 0, 0));
        taskManager.updateTask(task1);
        System.out.println("task1Id = " + task1.getId());
        Assertions.assertEquals(task1, task);

        task1.setId(3);
        System.out.println("task1Id = " + task1.getId());

        Assertions.assertNotEquals(task1, task);

    }

    @Test
    void deleteSubtask() {
        Epic epic = taskManager.getEpicById(2);
        Subtask subtask1 = taskManager.getSubtaskById(3);
        Subtask subtask2 = taskManager.getSubtaskById(4);

        taskManager.deleteSubtasks();
        assertEquals(0, taskManager.getSubtasksMap().size(), "SubtasksMap is Not Empty");

    }

    @Test
    void epicStatusTest() {
        Epic epic = taskManager.getEpicById(2);
        Subtask subtask = taskManager.getSubtaskById(3);
        Subtask subtask1 = taskManager.getSubtaskById(4);

        //Подзадачи эпика NEW
        Assertions.assertEquals(Status.NEW, epic.getStatus(), "Epic status is not a NEW");

        //Подзадача эпика IN_PROGRESS
        Subtask subtask2 = new Subtask("Test addNewEpicTest", "addNewEpicTest description",
                3, Status.IN_PROGRESS, 2,
                1000, LocalDateTime.of(2024, 7, 2, 0, 0));
        taskManager.updateSubtask(subtask2);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Epic status is not a NEW");

        //Подзадача эпика NEW и DONE
        Subtask subtask3 = new Subtask("Test addNewEpicTest", "addNewEpicTest description",
                3, Status.DONE, 2,
                1000, LocalDateTime.of(2024, 7, 2, 0, 0));
        taskManager.updateSubtask(subtask3);
        Assertions.assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Epic status is not a NEW");

        //Подзадача эпика DONE
        Subtask subtask4 = new Subtask("Test addNewEpicTest", "addNewEpicTest description",
                3, Status.DONE, 2,
                1000, LocalDateTime.of(2024, 7, 2, 0, 0));
        taskManager.updateSubtask(subtask4);
        Subtask subtask5 = new Subtask("Test addNewEpicTest2", "addNewEpicTest2 description2",
                4, Status.DONE, 2,
                1000, LocalDateTime.of(2024, 7, 3, 0, 0));
        taskManager.updateSubtask(subtask5);
        Assertions.assertEquals(Status.DONE, epic.getStatus(), "Epic status is not a NEW");

    }
}
