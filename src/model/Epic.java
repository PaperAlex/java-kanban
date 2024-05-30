package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksListIds = new ArrayList<>();

    public Epic(String name, String description, Integer id, Status status) {
        super(name, description, id, status);
    }

    public ArrayList<Integer> getSubtasksListIds() {
        return subtasksListIds;
    }

    public void setSubtasksListIds(ArrayList<Integer> subtasksListIds) {
        this.subtasksListIds = subtasksListIds;
    }
    public void addSubtasksListIds (int s) {
        subtasksListIds.add(s);
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