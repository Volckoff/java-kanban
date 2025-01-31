package managers;

import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryTaskManagerTest {

    TaskManager manager;

    @BeforeEach
    public void initManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void initialsTaskManager() {
        assertNotNull(manager);
        assertEquals(0, manager.getTasks().size(), "Task list should be empty");
        assertEquals(0, manager.getEpics().size(), "Epic list should be empty");
        assertEquals(0, manager.getSubtasks().size(), "Subtask list should be empty");
    }

    @Test
    public void testAddTask() {
        Task task = new Task("Test 1", "Description 1", Status.NEW);
        manager.addNewTask(task);
        assertEquals(1, manager.getTasks().size(), "task should be added");
        Task addedTask = manager.getTasks().get(0);
        assertEquals(task, addedTask, "added task id should be set");
        Task byIdTask = manager.getTask(task.getId());
        assertEquals(task, byIdTask, "should be found");
    }

    @Test
    public void testAddEpic() {
        Epic epic = new Epic("Test 1", "Description");
        manager.addNewEpic(epic);
        assertEquals(1, manager.getEpics().size(), "epic should be added");
        Epic addedEpic = manager.getEpics().get(0);
        assertEquals(epic, addedEpic, "added epic id should be set");
        Epic byIdEpic = manager.getEpic(epic.getId());
        assertEquals(epic, byIdEpic, "should be found");
    }

    @Test
    public void testAddSubtask() {
        Epic epic = new Epic("Test 1", "Description 1");
        final int epicId = manager.addNewEpic(epic);
        Subtask subtask = new Subtask("Task.Subtask 1-1", "description1-1", Status.DONE, epicId);
        manager.addNewSubtask(subtask);
        assertEquals(1, manager.getSubtasks().size(), "subtask should be added");
        Subtask addedSubtask = manager.getSubtasks().get(0);
        assertEquals(subtask, addedSubtask, "added subtask should be set");
        Subtask byIdSubtask = manager.getSubtask(subtask.getId());
        assertEquals(subtask, byIdSubtask, "should be found");
    }

    @Test
    public void taskNotChangeAfterAddInMap() {
        int id = 1;
        String name = "Test 1";
        String description = "Description 1";
        Status status = Status.NEW;
        Task taskBefore = new Task(id, name, description, status);
        manager.addNewTask(taskBefore);
        Task taskAfter = manager.getTask(taskBefore.getId());
        assertEquals(id, taskAfter.getId());
        assertEquals(description, taskAfter.getDescription());
        assertEquals(name, taskAfter.getName());
        assertEquals(status, taskAfter.getStatus());
    }

    @Test
    public void testNotConflictTaskWithAndWithoutId() {
        Task task1 = new Task("Test 1", "Description", Status.NEW);
        Task task2 = new Task(1, "Test 2", "Description 2", Status.IN_PROGRESS);
        manager.addNewTask(task1);
        manager.addNewTask(task2);
        assertEquals(2, manager.getTasks().size(), "both tasks should be added");
        assertEquals(1, task1.getId(), "autogenerate id should be 1");
        assertEquals(2, task2.getId(), "task predefined id should be change");
    }

    @Test
    public void testRemoveSubtaskFromEpic() {
        Epic epic1 = new Epic(1, "Test 1", "Description 1");
        final int epic1Id = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Task.Subtask 1-1", "description1-1", Status.DONE, epic1Id);
        final int subtask1Id = manager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Task.Subtask 2-1", "description2-1", Status.NEW, epic1Id);
        final int subtask2Id = manager.addNewSubtask(subtask2);
        manager.clearAllSubtasks();
        assertEquals(0, epic1.getSubtasksId().size(), "Subtasks should be deleted from SubtasksId");
    }

    @Test
    public void testChangeEpicStatus() {
        Epic epic1 = new Epic(1, "Test 1", "Description 1");
        final int epic1Id = manager.addNewEpic(epic1);
        assertEquals(Status.NEW, epic1.getStatus(), "Status should be NEW");
        Subtask subtask1 = new Subtask("Task.Subtask 1-1", "description1-1", Status.DONE, epic1Id);
        final int subtask1Id = manager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Task.Subtask 2-1", "description2-1", Status.NEW, epic1Id);
        final int subtask2Id = manager.addNewSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Status should be change to IN_PROGRESS ");
    }

    @Test
    public void testRemoveAllTasks() {
        Task task = new Task("Test 1", "Description 1", Status.NEW);
        manager.addNewTask(task);
        Task task2 = new Task("Test 2", "Description 2", Status.NEW);
        manager.addNewTask(task2);
        assertEquals(2, manager.getTasks().size(), "Tasks should be added");
        manager.clearAllTasks();
        assertEquals(0, manager.getTasks().size(), "Tasks should be deleted");
    }

    @Test
    public void testRemoveAllEpics() {
        Epic epic1 = new Epic(1, "Test 1", "Description 1");
        manager.addNewEpic(epic1);
        Epic epic2 = new Epic(2, "Test 2", "Description 2");
        manager.addNewEpic(epic1);
        assertEquals(2, manager.getEpics().size(), "Epics should be added");
        manager.clearAllEpics();
        assertEquals(0, manager.getEpics().size(), "Epics should be deleted");
    }

    @Test
    public void testRemoveAllSubtasks() {
        Epic epic1 = new Epic(1, "Test 1", "Description 1");
        final int epic1Id = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask("Task.Subtask 1-1", "description1-1", Status.DONE, epic1Id);
        manager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Task.Subtask 1-2", "description1-1", Status.DONE, epic1Id);
        manager.addNewSubtask(subtask2);
        assertEquals(2, manager.getSubtasks().size(), "Subtasks should be added");
        manager.clearAllSubtasks();
        assertEquals(0, manager.getSubtasks().size(), "Subtasks should be deleted");

    }
}