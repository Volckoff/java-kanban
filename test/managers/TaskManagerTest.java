package managers;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class TaskManagerTest<T extends TaskManager> {

    TaskManager taskManager;
    protected Task task1;
    protected Task task2;
    protected Epic epic1;
    protected Epic epic2;
    protected Subtask subtask1;
    protected Subtask subtask2;

    @BeforeEach
    public void beforeEach() {
        taskManager = initTaskManager();
        task1 = new Task("Test 1", "Description 1", Status.IN_PROGRESS,
                LocalDateTime.now(), Duration.ofMinutes(5));
        task2 = new Task("Test 2", "Description 2", Status.IN_PROGRESS,
                LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(5));
        epic1 = new Epic(1, "Test 1", "Description 1");
        taskManager.addNewEpic(epic1);
        int epicId = epic1.getId();
        epic2 = new Epic(2, "Test 2", "Description 2");
        subtask1 = new Subtask("Test 1-1", "Description 1", Status.NEW,
                LocalDateTime.now().plusMinutes(45), Duration.ofMinutes(5), epicId);
        subtask2 = new Subtask("Test 1-2", "Description 2", Status.DONE,
                LocalDateTime.now().plusMinutes(60), Duration.ofMinutes(5), epicId);
    }


    @Test
    void getEpicSubtask() {
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(2, taskManager.getEpicSubtask(epic1.getId()).size(), "subtasks should be added" +
                " in List");
    }

    @Test
    void getTask() {
        taskManager.addNewTask(task1);
        int taskId = task1.getId();
        Task task3 = taskManager.getTask(taskId);
        assertEquals(task1, task3, "tasks should be equal");
    }

    @Test
    void getEpic() {
        taskManager.addNewEpic(epic1);
        int epicId = epic1.getId();
        Epic epic3 = taskManager.getEpic(epicId);
        assertEquals(epic1, epic3, "epics should be equal");
    }

    @Test
    void getSubtask() {
        taskManager.addNewSubtask(subtask1);
        int subtaskId = subtask1.getId();
        Subtask subtask3 = taskManager.getSubtask(subtaskId);
        assertEquals(subtask1, subtask3, "subtask should be equal");
    }

    @Test
    void addNewTask() {
        assertEquals(0, taskManager.getTasks().size(), "list should be empty");
        taskManager.addNewTask(task1);
        assertEquals(1, taskManager.getTasks().size(), "task should be added in map");
    }

    @Test
    void addNewEpic() {
        assertEquals(1, taskManager.getEpics().size(), "list should contain only epic1 ");
        taskManager.addNewEpic(epic2);
        assertEquals(2, taskManager.getEpics().size(), "epic should be added in map");
    }

    @Test
    void addNewSubtask() {
        assertEquals(0, taskManager.getSubtasks().size(), "list should be empty");
        taskManager.addNewSubtask(subtask1);
        assertEquals(1, taskManager.getSubtasks().size(), "subtask should be added in map");
    }

    @Test
    void updateTask() {
        assertEquals("Test 1", task1.getName(), "Task has the wrong name ");
        task1.setName("Test1 Upd");
        taskManager.updateTask(task1);
        assertEquals("Test1 Upd", task1.getName(), "The name field has not been updated");
    }

    @Test
    void updateEpic() {
        assertEquals("Test 1", epic1.getName(), "Epic has the wrong name ");
        epic1.setName("Test1 Upd");
        taskManager.updateEpic(epic1);
        assertEquals("Test1 Upd", epic1.getName(), "The name field has not been updated");
    }

    @Test
    void updateSubtask() {
        assertEquals("Test 1-1", subtask1.getName(), "Epic has the wrong name ");
        subtask1.setName("Test1 Upd");
        taskManager.updateSubtask(subtask1);
        assertEquals("Test1 Upd", subtask1.getName(), "The name field has not been updated");
    }

    @Test
    void removeTaskForId() {
        taskManager.addNewTask(task1);
        int task1Id = task1.getId();
        taskManager.addNewTask(task2);
        assertEquals(2, taskManager.getTasks().size(), "tasks should be added in map");
        taskManager.removeTaskForId(task1Id);
        assertEquals(1, taskManager.getTasks().size(), "task1 should be removed");
    }

    @Test
    void removeEpicForId() {
        int epic2Id = taskManager.addNewEpic(epic2);
        assertEquals(2, taskManager.getEpics().size(), "epics should be added in map");
        taskManager.removeEpicForId(epic2Id);
        assertEquals(1, taskManager.getEpics().size(), "epic2 should be removed");
    }

    @Test
    void removeSubtaskForId() {
        int subtask1Id = taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(2, taskManager.getSubtasks().size(), "subtasks should be added in map");
        taskManager.removeSubtaskForId(subtask1Id);
        assertEquals(1, taskManager.getSubtasks().size(), "subtask1 should be removed");
    }

    @Test
    void clearAllTasks() {
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        assertEquals(2, taskManager.getTasks().size());
        taskManager.clearAllTasks();
        assertEquals(0, taskManager.getTasks().size(), "map should be empty");
    }

    @Test
    void clearAllEpics() {
        taskManager.addNewEpic(epic2);
        assertEquals(2, taskManager.getEpics().size());
        taskManager.clearAllEpics();
        assertEquals(0, taskManager.getEpics().size(), "map should be empty");
    }

    @Test
    void clearAllSubtasks() {
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(2, taskManager.getSubtasks().size());
        taskManager.clearAllSubtasks();
        assertEquals(0, taskManager.getSubtasks().size(), "map should be empty");
    }

    @Test
    void getHistory() {
        taskManager.addNewTask(task1);
        int taskId = task1.getId();
        taskManager.getTask(taskId);
        taskManager.getHistory();
        assertEquals(1, taskManager.getHistory().size(), "task1 should be added in history");
    }

    @Test
    public void taskNotChangeAfterAddInMap() {
        int id = 2;
        String name = "Test 1";
        String description = "Description 1";
        Status status = Status.NEW;
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(10);
        Task taskBefore = new Task(id, name, description, status,startTime,duration);
        taskManager.addNewTask(taskBefore);
        Task taskAfter = taskManager.getTask(taskBefore.getId());
        assertEquals(id, taskAfter.getId());
        assertEquals(description, taskAfter.getDescription());
        assertEquals(name, taskAfter.getName());
        assertEquals(status, taskAfter.getStatus());
    }

    @Test
    public void testNotConflictTaskWithAndWithoutId() {
        Task taskWithId = new Task("Test 1", "Description", Status.NEW,
                LocalDateTime.now(),Duration.ofMinutes(5));
        Task taskWithoutId = new Task(1, "Test 2", "Description 2", Status.IN_PROGRESS,
                LocalDateTime.now().plusMinutes(20), Duration.ofMinutes(10));
        taskManager.addNewTask(taskWithId);
        taskManager.addNewTask(taskWithoutId);
        assertEquals(2, taskManager.getTasks().size(), "both tasks should be added");
        assertEquals(2, taskWithId.getId(), "autogenerate id should be 1");
        assertEquals(3, taskWithoutId.getId(), "task predefined id should be change");
    }

    @Test
    public void testRemoveSubtaskFromEpic() {
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.clearAllSubtasks();
        assertEquals(0, epic1.getSubtasksId().size(), "Subtasks should be deleted from SubtasksId");
    }

    @Test
    public void testChangeEpicStatus() {
        assertEquals(Status.NEW, epic1.getStatus(), "Status should be NEW");
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Status should be change to IN_PROGRESS");
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        assertEquals(Status.DONE, epic1.getStatus(), "Status should be change to DONE");
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.NEW);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        assertEquals(Status.NEW, epic1.getStatus(), "Status should be change to NEW");
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Status should be change to IN_PROGRESS");
    }

    @Test
    public void taskIntersectionTest() {
        Task task3 = new Task("Test 1", "Description 1", Status.IN_PROGRESS,
                LocalDateTime.now().minusMinutes(2), Duration.ofMinutes(15));
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task3);
        assertEquals(1, taskManager.getPrioritizedTasks().size(), "should not add a task " +
                "that overlaps with time");
    }

    TaskManager initTaskManager() {
        return Managers.getDefault();
    }

}
