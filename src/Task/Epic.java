package Task;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    protected ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String name, String description, Status status) {
        super(name, description, status);
    }

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
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

