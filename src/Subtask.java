public class Subtask extends Task {

    Epic epic;
    public Subtask(String name, String description, Integer id, Status status, Epic epic) {
        super(name, description, id, status);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}