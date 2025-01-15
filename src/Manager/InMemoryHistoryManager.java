package Manager;

import Task.*;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (task == null) {
           return;
        }
        if (history.size() > 10) {
            history.removeFirst();
        }
            history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
