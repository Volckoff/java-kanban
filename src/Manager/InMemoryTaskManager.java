package Manager;
import Task.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int idCounter = 0;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList<Task> getTasks() { return new ArrayList<>(tasks.values()); }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Subtask> getEpicSubtask(int epicId) {
        ArrayList<Subtask> getEpicSubtaskList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epicId) {
                getEpicSubtaskList.add(subtask);
            }
        }
        return getEpicSubtaskList;
    }

    @Override
    public Task getTask(int id) {
        final Task task = tasks.get(id);
        historyManager.addTask(task);
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.addTask(subtask);
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.addTask(epic);
        return epic;
    }

    @Override
    public int addNewTask(Task task) {
        final int id = ++idCounter;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        final int id = ++idCounter;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        final int id = ++idCounter;
        subtask.setId(id);
        Epic epic = getEpic(subtask.getEpicId());
        if (epic == null) {
            return null;
        }
        subtasks.put(id, subtask);
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(epic);
        return id;
    }

    @Override
    public void updateEpicStatus(Epic epic) {
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

    @Override
    public void updateTask(Task task) {
        if (task == null) {
            return;
        }
        final Task updTask = tasks.get(task.getId());
        updTask.setName(task.getName());
        updTask.setDescription(task.getDescription());
        tasks.put(task.getId(), updTask);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null) {
            return;
        }
        final Epic updEpic = epics.get(epic.getId());
        updEpic.setName(epic.getName());
        updEpic.setDescription(epic.getDescription());
        ArrayList <Integer> updSubtaskId = epic.getSubtasksId();
        epic.setSubtasksId(updSubtaskId);
        epics.put(epic.getId(), updEpic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask == null) {
            return;
        }
        subtask.setName(subtask.getName());
        final Subtask updSubtask = subtasks.get(subtask.getId());
        updSubtask.setName(subtask.getName());
        updSubtask.setDescription(subtask.getDescription());
        int epicId = subtask.getEpicId();
        updSubtask.setEpicId(epicId);
        subtasks.put(subtask.getId(), updSubtask);
    }

    @Override
    public void removeTaskForId(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpicForId(int id) {
        ArrayList<Integer> removeSub = new ArrayList<>(getEpic(id).getSubtasksId());
        for (Integer subId : removeSub) {
            subtasks.remove(subId);
        }
        epics.remove(id);
    }

    @Override
    public void removeSubtaskForId(int id) {
        Epic epic = getEpic(getSubtask(id).getEpicId());
        epic.removeSubtask(id);
        subtasks.remove(id);
        updateEpicStatus(epic);
    }

    @Override
    public void clearAllTasks() {
        tasks.clear();
    }

    @Override
    public void clearAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
