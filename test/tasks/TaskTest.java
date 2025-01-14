package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Task.*;
import Manager.*;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    TaskManager manager;

    @BeforeEach
    public void initManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void testEqualsById() {
        Task task1 = new Task (1, "Test 1", "Description 1", Status.NEW);
        Task task2 = new Task(1, "Test 2", "Description 2", Status.IN_PROGRESS);
        assertEquals(task1, task2, "should be compared by Id");
    }
}