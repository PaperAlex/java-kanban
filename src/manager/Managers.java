package manager;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
//        return new InMemoryTaskManager(getDefaultHistory());
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getDefaultFile() {
        return new FileBackedTaskManager(new File("./resources/java-kanban.csv"));
    }
}
