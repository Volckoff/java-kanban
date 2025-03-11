package managers;

import manager.CSVTaskFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CSVTaskFormatTest {

    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setUp() {
        task = new Task(1, "Task 1", "Description 1", Status.NEW,
                LocalDateTime.of(1, 1, 1, 1, 1), Duration.ofMinutes(15));
        epic = new Epic(2, "Epic 1", "Description 1");
        subtask = new Subtask(3, "Subtask 1", "Description 1", Status.NEW,
                LocalDateTime.of(2, 2, 2, 2, 2), Duration.ofMinutes(10), 2);
    }

    @Test
    void testToString() {
        assertEquals("1,TASK,Task 1,NEW,Description 1,0001-01-01T01:01,15", CSVTaskFormat.toString(task));
        assertEquals("2,EPIC,Epic 1,NEW,Description 1,null,0", CSVTaskFormat.toString(epic));
        assertEquals("3,SUBTASK,Subtask 1,NEW,Description 1,0002-02-02T02:02,10,2",
                CSVTaskFormat.toString(subtask));
    }

    @Test
    void taskFromString() {
        assertEquals(task, CSVTaskFormat.taskFromString("1,TASK,Task 1,NEW,Description 1,0001-01-01T01:01,15"));
        assertEquals(epic, CSVTaskFormat.taskFromString("2,EPIC,Epic 1,NEW,Description 1,0001-01-01T01:01,15,0"));
        assertEquals(subtask, CSVTaskFormat.taskFromString("3,SUBTASK,Subtask 1,NEW," +
                "Description 1,0002-02-02T02:02,10,2"));
    }
}