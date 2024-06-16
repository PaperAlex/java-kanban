package modelTest;

import model.Status;
import model.Task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class TaskTest {

    /**
     * Проверка, что экземпляры класса Task равны друг другу, если равен их id;
     */
    @Test
    public void tasksEqualsById() {
        Task task1 = new Task("Написать код", "Писать код на JAVA", 0, Status.NEW);
        Task task2 = new Task("Написать код", "Писать код на JAVA", 0, Status.NEW);
        Assertions.assertEquals(task1, task2);
    }
}