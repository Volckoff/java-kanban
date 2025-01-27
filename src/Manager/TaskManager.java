package Manager;

import Task.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

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
}
