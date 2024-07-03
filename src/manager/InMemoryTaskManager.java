package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.List;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int idGenerator = 0;
    /* 1. Возможность хранить задачи всех типов. */
    private final Map<Integer, Task> tasksMap = new HashMap<Integer, Task>();
    private final Map<Integer, Subtask> subtasksMap = new HashMap<Integer, Subtask>();
    private final Map<Integer, Epic> epicsMap = new HashMap<Integer, Epic>();
//    private final HistoryManager taskManager;

//    public InMemoryTaskManager(HistoryManager defaultHistory) {
//        taskManager = defaultHistory;
//    }

    private final HistoryManager taskManager = Managers.getDefaultHistory();

    /**
     * Методы для каждого из типа задач(Задача/Эпик/Подзадача):
     * a. Получение списка всех задач.
     */
    @Override
    public List<Task> getTasksMap() {
        return new ArrayList<>(tasksMap.values());
    }

    @Override
    public List<Epic> getEpicsMap() {
        return new ArrayList<>(epicsMap.values());
    }

    @Override
    public List<Subtask> getSubtasksMap() {
        return new ArrayList<>(subtasksMap.values());
    }

    /**
     * b. Удаление всех задач.
     */
    @Override
    public void deleteTasks() {
        for (Task task : tasksMap.values()) {
            taskManager.remove(task.getId());
        }
        tasksMap.clear();
    }

    @Override
    public void deleteEpics() {
        for (Task task : epicsMap.values()) {
            taskManager.remove(task.getId());
        }
        epicsMap.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Task task : subtasksMap.values()) {
            taskManager.remove(task.getId());
        }
        subtasksMap.clear();
        for (Epic epic : epicsMap.values()) {
            updateEpicStatus(epic);
        }
    }

    /**
     * c. Получение по идентификатору.
     */
    @Override
    public Task getTaskById(int id) {
        Task task = tasksMap.getOrDefault(id, null);
        taskManager.addHistory(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epicsMap.getOrDefault(id, null);
        taskManager.addHistory(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasksMap.getOrDefault(id, null);
        taskManager.addHistory(subtask);
        return subtask;
    }

    @Override
    public List<Subtask> getSubTasksByEpicId(int id) {
        List<Integer> tasksList = getSubTasksIdByEpicId(id);
        List<Subtask> subTasksList = new ArrayList<>();
        for (Integer taskId : tasksList) {
            subTasksList.add(subtasksMap.get(taskId));
        }
        return subTasksList;
    }

    /**
     * d. Создание. Сам объект должен передаваться в качестве параметра.
     */
    @Override
    public void addTask(Task task) {
        task.setId(++idGenerator);
        tasksMap.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(++idGenerator);
        epic.setStatus(Status.NEW);
        epicsMap.put(epic.getId(), epic);
    }

    @Override
    public List<Integer> getSubTasksIdByEpicId(int id) {
        return epicsMap.get(id).getSubtasksListIds();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(++idGenerator);
        subtasksMap.put(subtask.getId(), subtask);

        int epicId = subtask.getEpicId();
        getSubTasksIdByEpicId(epicId).add(subtask.getId());
    }

    /**
     * e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
     */
    @Override
    public void updateTask(Task task) {
        tasksMap.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epic.setSubtasksListIds(getSubTasksIdByEpicId(epic.getId()));
        epicsMap.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasksMap.put(subtask.getId(), subtask);
        int epicsId = subtask.getEpicId();
        updateEpicStatus(getEpicById(epicsId));
    }

    /**
     * f. Удаление по идентификатору.
     */
    @Override
    public void deleteTaskById(int id) {
        taskManager.remove(id);
        tasksMap.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epicsMap.get(id);
        taskManager.remove(id);
        epicsMap.remove(id);
        epic.setSubtasksListIds(new ArrayList<>());

    }

    @Override
    public void deleteSubtaskById(int id) {
        int epicId = subtasksMap.get(id).getEpicId();
        Epic epic = getEpicById(epicId);
        epic.getSubtasksListIds().remove(Integer.valueOf(id));
        updateEpicStatus(epic);
        taskManager.remove(id);
        subtasksMap.remove(id);
    }

    /**
     * 4. Управление статусами эпика b.
     * Собираем все статусы сабтасков
     * Удаляем дубли, если статусов больше 1-го то эпик IN_PROGRESS.
     * Если остался один статус, то он становится статусом эпика
     */
    private void updateEpicStatus(Epic epic) {
        List<Integer> epicsSubtasks = epic.getSubtasksListIds(); // лист с сабтасками эпика
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

    @Override
    public List<Task> getHistory() {
        return taskManager.getHistory();
    }
}