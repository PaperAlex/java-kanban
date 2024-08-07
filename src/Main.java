import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.File;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        /* Старый менеджер */
        /* TaskManager taskManager = Managers.getDefault(); */

        /*
          Новый менеджер FileBackedTaskManager
         */
        TaskManager taskManager = new Managers().getDefaultFile();


        Task task1 = new Task("Написать код", "Писать код на JAVA", 0, Status.NEW,
                1000, LocalDateTime.of(2024, 7,1,0,0));
        Task task2 = new Task("Написать тест", "Написать тест на JAVA", 0, Status.NEW,
                10000, LocalDateTime.of(2024, 7,2,0,0));
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Сходить в магазин", "Купить продукты", 0, Status.NEW);
        Epic epic2 = new Epic("Зайти в спорт зал", "Потренировать ноги", 0, Status.NEW);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask epic1subtask1 = new Subtask("Мясо", "Индейка", 0, Status.NEW, 3,
                1000, LocalDateTime.of(2024, 8,3,0,0));
        taskManager.addSubtask(epic1subtask1);
        Subtask epic1subtask2 = new Subtask("Напитки", "Сок", 0, Status.NEW, 3,
                2000, LocalDateTime.of(2024, 8,4,0,0));
        taskManager.addSubtask(epic1subtask2);

        Subtask epic2subtask3 = new Subtask("Жим", "Пять подходов", 0, Status.NEW, 4,
                3000, LocalDateTime.of(2024, 6,5,0,0));
        taskManager.addSubtask(epic2subtask3);

        /* a. Получение списка всех задач. */
        System.out.println("Получение списка всех задач.");
        System.out.println("Tasks: " + taskManager.getTasksMap());
        System.out.println();
        System.out.println("Epics: " + taskManager.getEpicsMap());
        System.out.println();
        System.out.println("Subtasks: " + taskManager.getSubtasksMap());
        System.out.println("---");



        /* e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра. */
        Task task1v2 = new Task("Написать код", "Писать код на JAVA", 1, Status.IN_PROGRESS,
                1000, LocalDateTime.of(2024, 7,1,0,0));
        Epic epic1v2 = new Epic("Сходить на рынок", "Купить продукты", 3, Status.NEW);
        Subtask epic1subtask2v2 = new Subtask("Напитки", "Сок", 6, Status.IN_PROGRESS, 3,
                1000, LocalDateTime.of(2024, 8,2,0,0));

        taskManager.updateTask(task1v2);
        taskManager.updateSubtask(epic1subtask2v2);
        /* Смена статуса после обновления подзадачи эпика */
        taskManager.updateEpic(epic1v2);

        /* c. Получение по идентификатору. */
        System.out.println("Печать обновленных задач, вызов по id:");
        System.out.println("Updated: " + taskManager.getTaskById(1));
        System.out.println("Updated: " + taskManager.getEpicById(3));
        System.out.println("Updated: " + taskManager.getSubtaskById(6));
        System.out.println("---");

        Task task1v3 = new Task("Написать кОд", "Писать код на JAVA", 1, Status.DONE,
                1000, LocalDateTime.of(2024, 7,1,0,0));
        taskManager.updateTask(task1v3);
        System.out.println("Updated: " + taskManager.getTaskById(1));

        System.out.println("printAllTasks");
        printAllTasks(taskManager);
        System.out.println("---");

        System.out.println("fileBackedTaskManager");
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(
                new File("resources/java-kanban.csv"));
        System.out.println("Tasks: " + fileBackedTaskManager.getTasksMap());
        System.out.println();
        System.out.println("Epics: " + fileBackedTaskManager.getEpicsMap());
        System.out.println();
        System.out.println("Subtasks: " + fileBackedTaskManager.getSubtasksMap());
        System.out.println("---");
        System.out.println();

        /* f. Удаление по идентификатору. */
        System.out.println("Удаляем таск, сабтаск и эпик по id:");
        taskManager.deleteTaskById(1);
        taskManager.deleteSubtaskById(6);
        taskManager.deleteEpicById(4);


        System.out.println("Печатаем список всех задач после удаления:");
        System.out.println("Tasks: " + taskManager.getTasksMap());
        System.out.println("Epics: " + taskManager.getEpicsMap());
        System.out.println("Subtasks: " + taskManager.getSubtasksMap());
        System.out.println();


        taskManager.deleteSubtasks();
        taskManager.deleteEpics();
        taskManager.deleteTasks();

        System.out.println("Печатаем список всех задач после удаления всего:");
        System.out.println("Tasks: " + taskManager.getTasksMap());
        System.out.println("Epics: " + taskManager.getEpicsMap());
        System.out.println("Subtasks: " + taskManager.getSubtasksMap());


    }

    private static void printAllTasks(TaskManager taskManager) {
        System.out.println("Задачи:");
        for (Task task : taskManager.getTasksMap()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : taskManager.getEpicsMap()) {
            System.out.println(epic);

            for (Task task : taskManager.getSubTasksByEpicId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : taskManager.getSubtasksMap()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : taskManager.getHistory()) {
            System.out.println(task);
        }
    }
}
