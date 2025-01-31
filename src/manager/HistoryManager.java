package manager;

import task.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void remove(int id);

    void addTask(Task task);
}
