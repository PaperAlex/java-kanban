package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    /**
     * Методы для каждого из типа задач(Задача/Эпик/Подзадача):
     * a. Получение списка всех задач.
     */
    List<Task> getTasksMap();

    List<Epic> getEpicsMap();

    List<Subtask> getSubtasksMap();

    /**
     * b. Удаление всех задач.
     */
    void deleteTasks();

    void deleteEpics();

    void deleteSubtasks();

    /**
     * c. Получение по идентификатору.
     */
    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    List<Subtask> getSubTasksByEpicId(int id);

    /**
     * d. Создание. Сам объект должен передаваться в качестве параметра.
     */
    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    /**
     * e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
     */
    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    /**
     * f. Удаление по идентификатору.
     */
    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(int id);

    List<Task> getHistory();


}
