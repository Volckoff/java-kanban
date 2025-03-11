package managers;

import manager.InMemoryTaskManager;
import org.junit.jupiter.api.BeforeEach;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
        task1 = new Task("Test 1", "Description 1", Status.IN_PROGRESS,
                LocalDateTime.now(), Duration.ofMinutes(5));
        task2 = new Task("Test 2", "Description 2", Status.IN_PROGRESS,
                LocalDateTime.now().plusMinutes(10), Duration.ofMinutes(5));
        epic1 = new Epic(1, "Test 1", "Description 1");
        taskManager.addNewEpic(epic1);
        int epicId = epic1.getId();
        epic2 = new Epic(2, "Test 2", "Description 2");
        subtask1 = new Subtask("Test 1-1", "Description 1", Status.NEW,
                LocalDateTime.now().plusMinutes(45), Duration.ofMinutes(5), epicId);
        subtask2 = new Subtask("Test 1-2", "Description 2", Status.DONE,
                LocalDateTime.now().plusMinutes(60), Duration.ofMinutes(5), epicId);
    }
}