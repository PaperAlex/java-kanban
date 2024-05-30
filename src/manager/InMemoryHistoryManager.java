package manager;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();
    private final static int HISTORY_MAX= 10;

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void addHistory(Task task) {
        if(history.size() >= HISTORY_MAX) {
            history.removeFirst();
        }
        history.add(task);
    }
}
