package managertest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import manager.TaskManager;
import manager.Managers;

public class ManagersTest {

    /**
     * Тест, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров;
     */
    @Test
    public void utilityClassAlwaysReturnsInitializedManagers() {
        TaskManager taskManager = new Managers().getDefault();
        Assertions.assertNotNull(taskManager);
    }
}
