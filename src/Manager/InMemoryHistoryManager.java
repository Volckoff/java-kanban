package Manager;

import Task.*;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    protected final int maxSize = 10;
    private final List<Task> history = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (history.size() < maxSize) {
            history.add(task);
        } else {
            history.remove(0);
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
