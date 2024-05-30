import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();


        Task task1 = new Task("Написать код", "Писать код на JAVA", 0, Status.NEW);
        Task task2 = new Task("Написать тест", "Написать тест на JAVA", 0, Status.NEW);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Epic epic1 = new Epic("Сходить в магазин", "Купить продукты", 0, Status.NEW);
        Epic epic2 = new Epic("Зайти в спорт зал", "Потренировать ноги", 0, Status.NEW);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        Subtask epic1subtask1 = new Subtask("Мясо", "Индейка", 0, Status.NEW, 3);
        taskManager.addSubtask(epic1subtask1);
        Subtask epic1subtask2 = new Subtask("Напитки", "Сок", 0, Status.NEW, 3);
        taskManager.addSubtask(epic1subtask2);

        Subtask epic2subtask3 = new Subtask("Жим", "Пять подходов", 0, Status.NEW, 4);
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
        Task task1v2 = new Task("Написать код", "Писать код на JAVA", 1, Status.IN_PROGRESS);
        Epic epic1v2 = new Epic("Сходить на рынок", "Купить продукты", 3, Status.NEW);
        Subtask epic1subtask2v2 = new Subtask("Напитки", "Сок", 6, Status.IN_PROGRESS, 3);

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

        System.out.println("printAllTasks");
        printAllTasks(taskManager);
        System.out.println("---");


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
