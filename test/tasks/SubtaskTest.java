package tasks;

import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SubtaskTest {

    Epic epic;
    Subtask subtask1;
    Subtask subtask2;

    TaskManager manager;

    @BeforeEach
    public void initManager() {
        manager = Managers.getDefault();
        epic = new Epic("Test 1", "Description 1");
        final int epicId1 = manager.addNewEpic(epic);
        subtask1 = new Subtask(1, "Test 1", "Description 1", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(10), epicId1);
        subtask2 = new Subtask(1, "Test 2", "Description 2", Status.IN_PROGRESS,
                LocalDateTime.now(), Duration.ofMinutes(10).plusMinutes(25), epicId1);
    }

    @Test
    public void testEqualsById() {
        assertEquals(subtask1, subtask2, "should be compared by Id");
    }

    @Test
    public void testNotAttachSubtaskItself() {
        manager.addNewSubtask(subtask1);
        Subtask subtask3 = new Subtask("Test 3", "Description 3", Status.NEW,
                LocalDateTime.now(), Duration.ofMinutes(10),subtask1.getId());
        assertNull(manager.addNewSubtask(subtask3));
    }

    @Test
    public void testUpdateSubtask() {
        assertEquals("Test 1", subtask1.getName(), "Epic has the wrong name ");
        subtask1.setName("Test1 Upd");
        manager.updateSubtask(subtask1);
        assertEquals("Test1 Upd", subtask1.getName(), "The name field has not been updated");
    }
}