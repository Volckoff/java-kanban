package manager;

import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int idCounter = 0;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getTasks() {
        return tasks.values().stream().toList();
    }

    @Override
    public List<Epic> getEpics() {
        return epics.values().stream().toList();
    }

    @Override
    public List<Subtask> getSubtasks() {
        return subtasks.values().stream().toList();
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
        if (task.getStartTime() == null || !isTaskIntersection(task)) {
            return 0;
        }
        final int id = ++idCounter;
        task.setId(id);
        tasks.put(id, task);
        prioritizedTasks.add(task);
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
        if (subtask.getStartTime() == null || !isTaskIntersection(subtask)) {
            return null;
        }
        final int id = ++idCounter;
        subtask.setId(id);
        Epic epic = getEpic(subtask.getEpicId());
        if (epic == null) {
            return null;
        }
        subtasks.put(id, subtask);
        epic.addSubtaskId(subtask.getId());
        prioritizedTasks.add(subtask);
        updateEpicStatus(epic);
        checkTermsForEpic(epic);
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

    @Override
    public void updateTask(Task task) {
        if (task == null) {
            return;
        }
        task.setName(task.getName());
        task.setDescription(task.getDescription());
        task.setStatus(task.getStatus());
        task.setStartTime(task.getStartTime());
        task.setDuration(task.getDuration());
        if (task.getStartTime() == null || !isTaskIntersection(task)) {
            return;
        }
        tasks.put(task.getId(),task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null) {
            return;
        }
        epic.setName(epic.getName());
        epic.setDescription(epic.getDescription());
        updateEpicStatus(epic);
        checkTermsForEpic(epic);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask == null) {
            return;
        }
        subtask.setName(subtask.getName());
        subtask.setDescription(subtask.getDescription());
        subtask.setStatus(subtask.getStatus());
        subtask.setStartTime(subtask.getStartTime());
        subtask.setDuration(subtask.getDuration());
        updateEpicStatus(epics.get(subtask.getEpicId()));
        checkTermsForEpic(epics.get(subtask.getEpicId()));
        if (subtask.getStartTime() == null || !isTaskIntersection(subtask)) {
            return;
        }
        subtasks.put(subtask.getId(), subtask);
    }

    @Override
    public void removeTaskForId(int id) {
        historyManager.remove(id);
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void removeEpicForId(int id) {
        ArrayList<Integer> removeSub = new ArrayList<>(getEpic(id).getSubtasksId());
        for (Integer subId : removeSub) {
            historyManager.remove(subId);
            prioritizedTasks.remove(subtasks.get(subId));
            subtasks.remove(subId);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubtaskForId(int id) {
        Epic epic = getEpic(getSubtask(id).getEpicId());
        epic.removeSubtask(id);
        historyManager.remove(id);
        prioritizedTasks.remove(subtasks.get(id));
        subtasks.remove(id);
        updateEpicStatus(epic);
        checkTermsForEpic(epic);
    }

    @Override
    public void clearAllTasks() {
        Set<Integer> idTasks = tasks.keySet();
        for (Integer idTask : idTasks) {
            historyManager.remove(idTask);
            prioritizedTasks.remove(tasks.get(idTask));
        }
        tasks.clear();
    }

    @Override
    public void clearAllEpics() {
        Set<Integer> idEpics = epics.keySet();
        for (Integer idEpic : idEpics) {
            historyManager.remove(idEpic);
        }
        Set<Integer> idSubtasks = subtasks.keySet();
        for (Integer idSubtask : idSubtasks) {
            historyManager.remove(idSubtask);
            prioritizedTasks.remove(subtasks.get(idSubtask));
        }
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void clearAllSubtasks() {
        Set<Integer> idSubtasks = subtasks.keySet();
        for (Integer idSubtask : idSubtasks) {
            historyManager.remove(idSubtask);
            prioritizedTasks.remove(subtasks.get(idSubtask));
        }
        for (Epic epic : epics.values()) {
            epic.clearSubtaskId();
            updateEpicStatus(epic);
            checkTermsForEpic(epic);
        }
        subtasks.clear();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private boolean isTaskIntersection(Task task) {
        if (getPrioritizedTasks().isEmpty()) {
            return true;
        }
        return getPrioritizedTasks().stream()
                .filter(newTask -> task.getStartTime().isBefore(newTask.getEndTime()) &&
                        task.getEndTime().isAfter(newTask.getStartTime()))
                .findAny()
                .isEmpty();
    }

    private void checkTermsForEpic(Epic epic) {
        LocalDateTime startTime = LocalDateTime.MAX;
        LocalDateTime endTime = LocalDateTime.MIN;
        Duration duration = Duration.ofMinutes(0);
        if (!epic.getSubtasksId().isEmpty()) {
            for (int id : epic.getSubtasksId()) {
                Subtask subtask = getSubtask(id);
                duration = duration.plus(subtask.getDuration());
                if (subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }
                if (subtask.getEndTime().isAfter(endTime)) {
                    endTime = subtask.getEndTime();
                }
            }
            epic.setStartTime(startTime);
            epic.setDuration(duration);
            epic.setEndTime(endTime);
        } else {
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
        }
    }
}
