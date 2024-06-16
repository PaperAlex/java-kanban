package modeltest;

import model.Epic;
import model.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class EpicTest {

    /**
     * Проверка, что наследники класса Task равны друг другу, если равен их id;
     */
    @Test
    public void epicsEqualsById() {
        Epic epic1 = new Epic("Сходить в магазин", "Купить продукты", 1, Status.NEW);
        Epic epic2 = new Epic("Сходить в магазин", "Купить продукты", 1, Status.NEW);
        Assertions.assertEquals(epic1, epic2);
    }
}