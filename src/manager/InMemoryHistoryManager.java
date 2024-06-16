package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    MyLinkedList history = new MyLinkedList();

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    @Override
    public void addHistory(Task task) {
        history.linkLast(task);
    }

    @Override
    public void remove(int id) {
        history.removeNode(id);
    }

    /**
     * Реализация аналога класса LinkedHashMap
     */
    private static class MyLinkedList {

        final Map<Integer, Node<Task>> historyMap = new HashMap<>();

        public void linkLast(Task task) {
            historyMap.put(task.getId(), new Node<>(task,null, null));
        }

        public void removeNode(int id) {
            historyMap.remove(id);
        }

        public List<Task> getTasks() {
            List<Task> listOfTasks = new ArrayList<>();
            for (Node<Task> node : historyMap.values()) {
                listOfTasks.add(node.task);
            }
            return listOfTasks;
        }
    }
}
