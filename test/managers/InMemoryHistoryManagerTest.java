package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Task.*;
import Manager.*;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager manager;

    @BeforeEach
    public void initManager() {
        manager = Managers.getDefaultHistory();
    }

    @Test
    public void initialisationManager() {
        assertNotNull(manager);
        assertEquals(0, manager.getHistory().size(), "history list should be empty");
    }

    @Test
    public void addTask() {
        Task task1 = new Task (1, "Test 1", "Description 1", Status.NEW);
        manager.addTask(task1);
        assertEquals(1, manager.getHistory().size(), "task should be added in history list");
    }

}