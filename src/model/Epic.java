package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subtasksListIds = new ArrayList<>();

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
    public String toString() {
        return "model.Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}