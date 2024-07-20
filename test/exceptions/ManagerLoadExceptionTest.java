package exceptions;

import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ManagerLoadExceptionTest {

    @Test
    void directoryTest() {
        File file = new File("resources/");

        assertThrows(ManagerLoadException.class, () -> FileBackedTaskManager.loadFromFile(file),
                "Error, path instead of file");
    }

}
