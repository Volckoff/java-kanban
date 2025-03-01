package tasks;

import manager.Managers;
import manager.TaskManager;
import task.Status;
import task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {
    Task task1;
    Task task2;

    TaskManager manager;

    @BeforeEach
    public void initManager() {
        manager = Managers.getDefault();
        task1 = new Task(1, "Test 1", "Description 1", Status.NEW);
        task2 = new Task(1, "Test 2", "Description 2", Status.IN_PROGRESS);
    }

    @Test
    public void testEqualsById() {
        assertEquals(task1, task2, "should be compared by Id");
    }

    @Test
    public void testUpdateTask() {
        assertEquals("Test 1", task1.getName(), "Task has the wrong name ");
        task1.setName("Test1 Upd");
        manager.updateTask(task1);
        assertEquals("Test1 Upd", task1.getName(), "The name field has not been updated");
    }
}