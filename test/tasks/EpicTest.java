package tasks;

import org.junit.jupiter.api.BeforeEach;
import Task.*;
import Manager.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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
}