package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TaskManager {
    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    ArrayList<Subtask> getEpicSubtask(int epicId);

    Task getTask(int id);

    Subtask getSubtask(int id);

    Epic getEpic(int id);

    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    Integer addNewSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void removeTaskForId(int id);

    void removeEpicForId(int id);

    void removeSubtaskForId(int id);

    void clearAllTasks();

    void clearAllEpics();

    void clearAllSubtasks();

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}
