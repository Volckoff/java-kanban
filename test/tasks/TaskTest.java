package tasks;

import manager.Managers;
import manager.TaskManager;
import task.Status;
import task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    TaskManager manager;

    @BeforeEach
    public void initManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void testEqualsById() {
        Task task1 = new Task(1, "Test 1", "Description 1", Status.NEW);
        Task task2 = new Task(1, "Test 2", "Description 2", Status.IN_PROGRESS);
        assertEquals(task1, task2, "should be compared by Id");
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task(0, "Test 1", "Description 1", Status.NEW);
        assertEquals("Test 1", task.getName(), "Epic has the wrong name ");
        task.setName("Test1 Upd");
        manager.updateTask(task);
        assertEquals("Test1 Upd", task.getName(), "The name field has not been updated");
    }
}