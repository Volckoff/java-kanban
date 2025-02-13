package task;

import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(int id, String name, String description) {
        super(id, name, description, Status.NEW);

    }

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public void setSubtasksId(ArrayList<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    public void addSubtaskId(int id) {
        if (subtasksId.contains(id)) {
            return;
        }
        if (this.id == id) {
            return;
        }
        subtasksId.add(id);

    }

    public void clearSubtaskId() {
        subtasksId.clear();
    }

    public void removeSubtask(int id) {
        subtasksId.remove(Integer.valueOf(id));
    }

}

