package modeltest;

import model.Epic;
import model.Status;
import model.Subtask;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class SubtaskTest {

    Epic epic1 = new Epic("Сходить в магазин", "Купить продукты", 0, Status.NEW);

    /**
     * Проверка, что наследники класса Task равны друг другу, если равен их id;
     */
    @Test
    public void subtaskEqualsById() {
        Subtask epic1subtask1 = new Subtask("Напитки", "Сок", 2, Status.NEW, 3);
        Subtask epic1subtask2 = new Subtask("Напитки", "Сок", 2, Status.NEW, 3);
        assertEquals(epic1subtask1, epic1subtask2);
    }
}
