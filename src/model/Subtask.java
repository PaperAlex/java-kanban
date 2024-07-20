package model;

import java.time.LocalDateTime;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String name, String description, Integer id, Status status, int epicId, long duration, LocalDateTime startTime) {
        super(name, description, id, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Integer id, Status status, int epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public Types getType() {
        return Types.SUBTASK;
    }


    @Override
    public String toString() {
        return "model.Subtask{" +
                "name='" + super.getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + super.getId() + '\'' +
                ", status='" + super.getStatus() + '\'' +
                ", epicId='" + epicId + '\'' +
                ", duration =" + super.getDuration().toMinutes() +
                ", startTime = " + super.getStartTimeToString() +
                ", endTime = " + super.getEndTimeToString() +
                '}';
    }
}