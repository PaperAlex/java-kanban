package managerTest;

import manager.HistoryManager;
import manager.Managers;
import model.Status;
import model.Task;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    HistoryManager inMemoryHistoryManager;
    @BeforeEach
    public void beforeEach() {
        inMemoryHistoryManager =  Managers.getDefaultHistory();
    }

    /**
     * Тест, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
     */
    @Test
    void addHistory() {
        Task task = new Task("Написать код", "Писать код на JAVA", 0, Status.NEW);

        inMemoryHistoryManager.addHistory(task);
        final List<Task> history = inMemoryHistoryManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

}
