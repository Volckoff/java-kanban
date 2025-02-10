package tasks;

import manager.Managers;
import manager.TaskManager;
import task.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    TaskManager manager;

    @BeforeEach
    public void initManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void testEqualsById() {
        Epic epic1 = new Epic(1, "Test 1", "Description 1");
        Epic epic2 = new Epic(1, "Test 2", "Description 2");
        assertEquals(epic1, epic2, "should be compared by Id");
    }

    @Test
    public void testSubtaskUniqueId() {
        Epic epic1 = new Epic(1, "Test 1", "Description 1");
        epic1.addSubtaskId(2);
        epic1.addSubtaskId(3);
        assertEquals(2, epic1.getSubtasksId().size(), "should add distinct subtask Id");
        epic1.addSubtaskId(3);
        assertEquals(2, epic1.getSubtasksId().size(), "should not add same subtask id twice");
    }

    @Test
    public void testNotAddItself() {
        Epic epic = new Epic(0, "Test 1", "Description 1");
        epic.addSubtaskId(0);
        assertEquals(0, epic.getSubtasksId().size(), "epic should not be add itself like subtask");
    }

    @Test
    public void testUpdateEpic() {
        Epic epic = new Epic(0, "Test 1", "Description 1");
        assertEquals("Test 1", epic.getName(), "Epic has the wrong name ");
        epic.setName("Test1 Upd");
        manager.updateEpic(epic);
        assertEquals("Test1 Upd", epic.getName(), "The name field has not been updated");
    }

    @Test
    public void testRemoveEpicForId() {
        Epic epic = new Epic(0, "Test 1", "Description 1");
        int epicId = manager.addNewEpic(epic);
        assertEquals(1, manager.getEpics().size(), "Epic should be added");
        manager.removeEpicForId(epicId);
        assertEquals(0, manager.getEpics().size(), "Epic should be added");
    }
}