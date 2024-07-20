package managertest;

import manager.InMemoryTaskManager;
import manager.TaskManager;

import org.junit.jupiter.api.BeforeEach;


public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        super.taskManager = new InMemoryTaskManager();
        makeTasks();
    }

}
