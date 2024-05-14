import java.util.ArrayList;

public class Epic extends Task {
    public static ArrayList<Integer> subtasksListIds = new ArrayList<>();

    public Epic(String name, String description, Integer id, Status status) {
        super(name, description, id, status);
    }

    public ArrayList<Integer> getSubtasksListIds() {
        return subtasksListIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        Epic.subtasksListIds = subtasksIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}