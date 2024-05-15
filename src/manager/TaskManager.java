package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TaskManager {
    private int idGenerator = 0;
    /* 1. Возможность хранить задачи всех типов. */
    private HashMap<Integer, Task> tasksMap = new HashMap<Integer, Task>();
    private HashMap<Integer, Subtask> subtasksMap = new HashMap<Integer, Subtask>();
    private HashMap<Integer, Epic> epicsMap = new HashMap<Integer, Epic>();

    /**
     * Методы для каждого из типа задач(Задача/Эпик/Подзадача):
     * a. Получение списка всех задач.
     */
    public HashMap<Integer, Task> getTasksMap() {
        return tasksMap;
    }

    public HashMap<Integer, Epic> getEpicsMap() {
        return epicsMap;
    }

    public HashMap<Integer, Subtask> getSubtasksMap() {
        return subtasksMap;
    }

    /**
     * b. Удаление всех задач.
     */
    public void deleteTasks() {
        tasksMap.clear();
    }

    public void deleteEpics() {
        epicsMap.clear();
    }

    public void deleteSubtasks() {
        subtasksMap.clear();
        for (Epic epic : epicsMap.values()) {
            updateEpicStatus(epic);
        }
    }

    /**
     * c. Получение по идентификатору.
     */
    public Task getTaskById(int id) {
        Task task = tasksMap.getOrDefault(id, null);
        return task;
    }

    public Epic getEpicById(int id) {
        return epicsMap.getOrDefault(id, null);
    }

    public Subtask getSubtaskById(int id) {
        return subtasksMap.getOrDefault(id, null);
    }

    /**
     * d. Создание. Сам объект должен передаваться в качестве параметра.
     */
    public void addTask(Task task) {
        task.setId(++idGenerator);
        tasksMap.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(++idGenerator);
        epic.setStatus(Status.NEW);
        epicsMap.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setId(++idGenerator);
        subtasksMap.put(subtask.getId(), subtask);
        Epic epic = subtask.getEpic();
        epic.addSubtasksListIds(subtask);
        updateEpicStatus(epic);

    }

    /**
     * e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
     */
    public void updateTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epic.setSubtasksListIds(epicsMap.get(epic.getId()).getSubtasksListIds());
        epicsMap.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    public void updateSubtask(Subtask subtask) {
        subtasksMap.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpic());
    }

    /**
     * f. Удаление по идентификатору.
     */
    public void deleteTaskById(int id) {
        tasksMap.remove(id);
    }

    public void deleteEpicById(int id) {
        Epic epic = epicsMap.get(id);
        epicsMap.remove(id);
        epic.setSubtasksListIds(new ArrayList<>());

    }

    public void deleteSubtaskById(int id) {
        Epic epic = subtasksMap.get(id).getEpic();
        /* Нужно удалить айди сабтаска из листа c id у эпика */
        epic.getSubtasksListIds().remove(Integer.valueOf(id));
        updateEpicStatus(epic);
        subtasksMap.remove(id);
    }

    /**
     * 4. Управление статусами эпика b.
     * Собираем все статусы сабтасков
     * Удаляем дубли, если статусов больше 1-го то эпик IN_PROGRESS.
     * Если остался один статус, то он становится статусом эпика
     */
    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> epicsSubtasks = epic.getSubtasksListIds(); // лист с сабтасками эпика
        ArrayList<Status> subtaskStatuses = new ArrayList<>();        // лист для сбора статусов
        if (epicsSubtasks.isEmpty()) {                                // если у эпика нет подзадач то статус NEW
            epic.setStatus(Status.NEW);
            return;
        }

        for (Integer subtaskId : epicsSubtasks) {               // запускаем цикл чтобы прошелся по собранным сабтаскам
            Subtask subtask = subtasksMap.get(subtaskId);
            if (subtask == null) {
                epic.setStatus(Status.NEW);
                return;
            }
            subtaskStatuses.add(subtask.getStatus());

        }
        Set<Status> set = new HashSet<>(subtaskStatuses);
        subtaskStatuses.clear();
        subtaskStatuses.addAll(set);

        if (subtaskStatuses.size() > 1) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(subtaskStatuses.getFirst());
        }

    }
}