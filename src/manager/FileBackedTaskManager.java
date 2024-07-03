package manager;

import exceptions.ManagerSaveException;
import model.Status;
import model.Types;
import model.Task;
import model.Epic;
import model.Subtask;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    private static final String HEADER = "id,type,name,status,description,epic_id";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    /**
     * Метод сохранения задачи в строку String toString(Task task) или переопределите базовый.
     */
    private String toString(Task task) {
        return task.getId() + ","
                + task.getType() + ","
                + task.getName() + ","
                + task.getStatus() + ","
                + task.getDescription();
    }

    private String toString(Subtask subtask) {
        return subtask.getId() + ","
                + subtask.getType() + ","
                + subtask.getName() + ","
                + subtask.getStatus() + ","
                + subtask.getDescription() + ","
                + subtask.getEpicId();
    }

    private String toString(Epic epic) {
        return epic.getId() + ","
                + epic.getType() + ","
                + epic.getName() + ","
                + epic.getStatus() + ","
                + epic.getDescription();
    }


    /**
     * Метод создания задачи из строки Task fromString(String value).
     */
    private static Task fromString(String value) {
        String[] csvString = value.split(",");
        int id = Integer.parseInt(csvString[0]);
        Types type = Types.valueOf(csvString[1]);
        String name = csvString[2];
        Status status = Status.valueOf(csvString[3]);
        String description = csvString[4];

        switch (type) {
            case TASK:
                return new Task(name, description, id, status);
            case EPIC:
                return new Epic(name, description, id, status);
            case SUBTASK:
                int epicId = Integer.parseInt(csvString[5]);
                return new Subtask(name, description, id, status, epicId);
        }
        return null;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(HEADER);
            writer.newLine();
            for (Task task : getTasksMap()) {
                writer.write(toString(task));
                writer.newLine();
            }
            for (Epic epic : getEpicsMap()) {
                writer.write(toString(epic));
                writer.newLine();
            }
            for (Subtask subtask : getSubtasksMap()) {
                writer.write(toString(subtask));
                writer.newLine();
            }

        } catch (IOException exception) {
            throw new ManagerSaveException(exception.getMessage());
        }
    }

    /**
     * Восстанавливать данные менеджера из файла при запуске программы.
     */
    public static FileBackedTaskManager loadFromFile(File file) {

        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.equals(HEADER)) {
                    continue;
                }
                Task task = fromString(line);
                switch (task.getType()) {
                    case TASK -> manager.addTask(task);
                    case EPIC -> manager.addEpic((Epic) task);
                    case SUBTASK -> manager.addSubtask((Subtask) task);
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException(exception.getMessage());
        }
        return manager;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public List<Task> getTasksMap() {
        return super.getTasksMap();
    }

    @Override
    public List<Epic> getEpicsMap() {
        return super.getEpicsMap();
    }

    @Override
    public List<Subtask> getSubtasksMap() {
        return super.getSubtasksMap();
    }

    @Override
    public List<Subtask> getSubTasksByEpicId(int id) {
        return super.getSubTasksByEpicId(id);
    }

    @Override
    public List<Integer> getSubTasksIdByEpicId(int id) {
        return super.getSubTasksIdByEpicId(id);
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }
}

