import Task.Task;
import Task.Epic;
import Task.Subtask;
import Task.Status;

import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    private int idCounter = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskArrayList = new ArrayList<>(tasks.values());
        return taskArrayList;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicArrayList = new ArrayList<>(epics.values());
        return epicArrayList;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtaskArrayList = new ArrayList<>(subtasks.values());
        return subtaskArrayList;
    }

    public ArrayList<Subtask> getEpicSubtask(int epicId) {
        ArrayList<Subtask> getEpicSubtaskList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epicId) {
                getEpicSubtaskList.add(subtask);
            }
        }
        return getEpicSubtaskList;
    }

    public Task getTask(int id) {
        Task task = tasks.get(id);
        return task;
    }

    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        return subtask;
    }

    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        return epic;
    }

    public int addNewTusk(Task task) {
        final int id = ++idCounter;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    public int addNewEpic(Epic epic) {
        final int id = ++idCounter;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    public int addNewSubtask(Subtask subtask) {
        final int id = ++idCounter;
        subtask.setId(id);
        Epic epic = getEpic(subtask.getEpicId());
        assert epic != null : "Такого эпика не существует";
        subtasks.put(id, subtask);
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(epic);
        return id;
    }

    void updateEpicStatus(Epic epic) {
        ArrayList<Status> subtaskStatus = new ArrayList<>();
        for (Subtask subStatus : subtasks.values()) {
            if (subStatus.getEpicId() == epic.getId()) {
                subtaskStatus.add(subStatus.getStatus());
            }
            int statusNew = 0;
            int statusDone = 0;
            Status stat;
            for (Status status : subtaskStatus) {
                if (status == Status.NEW) {
                    statusNew++;
                } else if (status == Status.DONE) {
                    statusDone++;
                }
            }
            if (subtaskStatus.isEmpty() || statusNew == subtaskStatus.size()) {
                stat = Status.NEW;
            } else if (statusDone == subtaskStatus.size()) {
                stat = Status.DONE;
            } else {
                stat = Status.IN_PROGRESS;
            }
            epic.setStatus(stat);
        }
    }

    void updateTask(Task task) {
        int id = task.getId();
        tasks.put(id, task);
    }

    void updateEpic(Epic epic) {
        int id = epic.getId();
        epics.put(id, epic);
    }

    void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        subtasks.put(id, subtask);
    }

    void removeTaskForId(int id) {
        tasks.remove(id);
    }

    void removeEpicForId(int id) {
        ArrayList<Integer> removeSub = new ArrayList<>(getEpic(id).getSubtasksId());
        for (Integer subId : removeSub) {
            subtasks.remove(subId);
        }
        epics.remove(id);
    }

    void removeSubtaskForId(int id) {
        Epic epic = getEpic(getSubtask(id).getEpicId());
        epic.removeSubtask(id);
        subtasks.remove(id);
        updateEpicStatus(epic);
    }


    public void clearAllTasks() {
        tasks.clear();
    }

    public void clearAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public void clearAllSubtasks() {
        ArrayList<Epic> epics = getEpics();
        for (Epic epic : epics) {
            epic.clearSubtaskId();
        }
        subtasks.clear();
    }

    public void printAll() {
        System.out.println("Задачи:");
        for (Task task : getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : getEpics()) {
            System.out.println(epic);
        }
        System.out.println("Подзадачи");
        for (Task subtask : getSubtasks()) {
            System.out.println(subtask);
        }
    }

}
