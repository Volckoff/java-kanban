package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Task.*;
import Manager.*;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    TaskManager manager;

    @BeforeEach
    public void initManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void testEqualsById() {
        Epic epic1 = new Epic("Test 1", "Description 1");
        final int epicId1 = manager.addNewEpic(epic1);
        Subtask subtask1 = new Subtask(1, "Test 1", "Description 1", Status.NEW, epicId1);
        Subtask subtask2 = new Subtask(1, "Test 2", "Description 2", Status.IN_PROGRESS, epicId1);
        assertEquals(subtask1, subtask2, "should be compared by Id");
    }

    @Test
    public void testNotAttachSubtaskItself() {
        Epic epic = new Epic("Test 1", "Description 1");
        int epicId = manager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("Test 2", "Description 2", Status.NEW, epicId);
        Integer subtaskId1 = manager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Test 3", "Description 3", Status.NEW, subtask1.getId());
        assertNull(manager.addNewSubtask(subtask2));
    }
}