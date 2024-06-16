package model;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String name, String description, Integer id, Status status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }


    @Override
    public String toString() {
        return "model.Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id + '\'' +
                ", status='" + status + '\'' +
                ", epicId='" + epicId + '\'' +
                '}';
    }
}