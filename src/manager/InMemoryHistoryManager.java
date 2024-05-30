package manager;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void addHistory(Task task) {
        if(history.size() >= 10) {
            history.removeFirst();
        }
        history.add(task);
    }
}
