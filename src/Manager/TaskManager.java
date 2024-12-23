package Manager;

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
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
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
        return tasks.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
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

    private void updateEpicStatus(Epic epic) {
        int statusNew = 0;
        int statusDone = 0;
        if (!epic.getSubtasksId().isEmpty()) {
            for (Integer id : epic.getSubtasksId()) {
                Subtask subtask = subtasks.get(id);
                Status stat = subtask.getStatus();
                if (stat == Status.NEW) {
                    statusNew++;
                } else if (stat == Status.DONE) {
                    statusDone++;
                }
            }
            if (statusNew == epic.getSubtasksId().size()) {
                epic.setStatus(Status.NEW);
            } else if (statusDone == epic.getSubtasksId().size()) {
                epic.setStatus(Status.DONE);
            } else {
                epic.setStatus(Status.IN_PROGRESS);
            }
        } else {
            epic.setStatus(Status.NEW);
        }
    }

    public void updateTask(Task task) {
        int id = task.getId();
        tasks.put(id, task);
    }

    public void updateEpic(Epic epic) {
        int id = epic.getId();
        epics.put(id, epic);
    }

    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        subtasks.put(id, subtask);
    }

    public void removeTaskForId(int id) {
        tasks.remove(id);
    }

    public void removeEpicForId(int id) {
        ArrayList<Integer> removeSub = new ArrayList<>(getEpic(id).getSubtasksId());
        for (Integer subId : removeSub) {
            subtasks.remove(subId);
        }
        epics.remove(id);
    }

    public void removeSubtaskForId(int id) {
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
        for (Epic epic : epics) {
            updateEpicStatus(epic);
        }
    }
}
