package Task;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    protected ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(int id, String name, String description) {
        super(id, name, description, Status.NEW);

    }

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public void addSubtaskId(int id) {
        subtasksId.add(id);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void clearSubtaskId() {
        subtasksId.clear();
    }

    public void removeSubtask(int id) {
        subtasksId.remove(Integer.valueOf(id));
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksId, epic.subtasksId);
    }

}

