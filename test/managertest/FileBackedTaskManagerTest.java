package managertest;

import manager.FileBackedTaskManager;
import manager.Managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File file;

    @BeforeEach
    public void beforeEach() throws IOException {
        super.taskManager = Managers.getDefaultFile();


        /**
         * Создаем таски и сохраняем в файлы
         */

        makeTasks();

        taskManager.getTaskById(1);
        taskManager.getSubtaskById(3);
        taskManager.getSubtaskById(4);
        taskManager.getEpicById(2);

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
