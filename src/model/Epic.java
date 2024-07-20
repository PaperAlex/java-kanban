package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasksListIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description, Integer id, Status status, long duration, LocalDateTime startTime,
                LocalDateTime endTime) {
        super(name, description, id, status, duration, startTime);
        this.endTime = endTime;
    }

    public Epic(String name, String description, Integer id, Status status) {
        super(name, description, id, status);
    }

    public List<Integer> getSubtasksListIds() {
        return subtasksListIds;
    }

    public void setSubtasksListIds(List<Integer> subtasksListIds) {
        this.subtasksListIds = subtasksListIds;
    }

    public void addSubtasksListIds(int s) {
        subtasksListIds.add(s);
    }

    public Types getType() {
        return Types.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
    public String getEndTimeToString() {
        if (endTime == null) {
            return "null";
        }
        return endTime.format(DATE_TIME_FORMATTER);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", id=" + super.getId() +
                ", status='" + super.getStatus() + '\'' +
                ", duration =" + super.getDuration().toMinutes() +
                ", startTime = " + getStartTimeToString() +
                ", endTime = " + getEndTimeToString() +
                '}';
    }
}