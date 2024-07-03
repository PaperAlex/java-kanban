package managertest;

import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private File file;
    private TaskManager taskManager;

    @BeforeEach
    public void beforeEach() throws IOException {
        taskManager = Managers.getDefaultFile();

        /**
         * Создаем таски и сохраняем в файлы
         */

        Task task = new Task("Test addHistory", "Test addHistory description", 0, Status.NEW);
        taskManager.addTask(task);

        Subtask subtask1 = new Subtask("Test addNewEpicTest", "addNewEpicTest description",
                0, Status.NEW, 2);
        Subtask subtask2 = new Subtask("Test addNewEpicTest2", "addNewEpicTest description2",
                0, Status.NEW, 2);
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description",
                0, Status.NEW);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

    }

    /**
     * Тест. Выгрузка из файла
     */
    @Test
    void loadFromFile() {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(new File("./resources/testing_java.csv"));

        assertEquals(1, fileBackedTaskManager.getTasksMap().size(), "TaskMap size not match");
        assertEquals(taskManager.getTasksMap(), fileBackedTaskManager.getTasksMap(), "Tasks are not equal");

        assertEquals(1, fileBackedTaskManager.getEpicsMap().size(), "EpicMap size not match");
        assertEquals(taskManager.getEpicsMap(), fileBackedTaskManager.getEpicsMap(), "Epics are not equals");

        assertEquals(2, fileBackedTaskManager.getSubtasksMap().size(), "SubtaskMap size not match");
        assertEquals(taskManager.getSubtasksMap(), fileBackedTaskManager.getSubtasksMap(), "Subtasks are not equal");

    }
}
