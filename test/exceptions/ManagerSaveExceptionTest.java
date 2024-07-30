package exceptions;

import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ManagerSaveExceptionTest {

    @Test
    void fileExistTest() {
        File file = new File("./resources/java-kanban.csv");

        assertDoesNotThrow(() -> FileBackedTaskManager.loadFromFile(file),
                "Error, path instead of file");
    }
}
