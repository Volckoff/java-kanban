package tasks;

import manager.Managers;
import manager.TaskManager;
import task.Epic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicTest {

    Epic epic1;
    Epic epic2;

    TaskManager manager;

    @BeforeEach
    public void initManager() {
        manager = Managers.getDefault();
        epic1 = new Epic(1, "Test 1", "Description 1");
        epic2 = new Epic(1, "Test 2", "Description 2");
    }

    @Test
    public void testEqualsById() {
        assertEquals(epic1, epic2, "should be compared by Id");
    }

    @Test
    public void testSubtaskUniqueId() {
        epic1.addSubtaskId(2);
        epic1.addSubtaskId(3);
        assertEquals(2, epic1.getSubtasksId().size(), "should add distinct subtask Id");
        epic1.addSubtaskId(3);
        assertEquals(2, epic1.getSubtasksId().size(), "should not add same subtask id twice");
    }

    @Test
    public void testNotAddItself() {
        epic1.addSubtaskId(1);
        assertEquals(0, epic1.getSubtasksId().size(), "epic should not be add itself like subtask");
    }

    @Test
    public void testUpdateEpic() {
        assertEquals("Test 1", epic1.getName(), "Epic has the wrong name ");
        epic1.setName("Test1 Upd");
        manager.updateEpic(epic1);
        assertEquals("Test1 Upd", epic1.getName(), "The name field has not been updated");
    }

    @Test
    public void testRemoveEpicForId() {
        int epicId = manager.addNewEpic(epic1);
        assertEquals(1, manager.getEpics().size(), "Epic should be added");
        manager.removeEpicForId(epicId);
        assertEquals(0, manager.getEpics().size(), "Epic should be added");
    }
}