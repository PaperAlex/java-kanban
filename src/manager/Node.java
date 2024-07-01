package manager;

import java.util.Objects;

public class Node<T> {
    public final T task;
    private final Node<T> next;
    private final Node<T> prev;

    public Node(T task, Node<T> prev, Node<T> next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(task, node.task) && Objects.equals(next, node.next) && Objects.equals(prev, node.prev);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, next, prev);
    }
}
