package managers;

import manager.HistoryManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryHistoryManagerTest {

    HistoryManager manager;

    @BeforeEach
    public void initManager() {
        manager = Managers.getDefaultHistory();
    }

    @Test
    @DisplayName("Инициализация менеджера")
    public void initialisationManager() {
        assertNotNull(manager);
        assertEquals(0, manager.getHistory().size(), "history list should be empty");
    }

    @Test
    @DisplayName("Добавление задачи")
    public void addTask() {
        Task task1 = new Task(1, "Test 1", "Description 1", Status.NEW);
        manager.addTask(task1);
        assertEquals(1, manager.getHistory().size(), "task should be added in history list");
    }

    @Test
    @DisplayName("Удаление задачи")
    public void remove() {
        Task task1 = new Task(1, "Test 1", "Description 1", Status.NEW);
        manager.addTask(task1);
        Task task2 = new Task(2, "Test 2", "Description 2", Status.NEW);
        manager.addTask(task2);
        assertEquals(2, manager.getHistory().size(), "both task should be added");
        manager.remove(task1.getId());
        assertEquals(1, manager.getHistory().size(), "task should be removed");
    }

    @Test
    @DisplayName("Дубликаты в истории")
    public void shouldNotDuplicateInHistory() {
        Task task1 = new Task(1, "Test 1", "Description 1", Status.NEW);
        manager.addTask(task1);
        Task task2 = new Task(2, "Test 2", "Description 2", Status.NEW);
        manager.addTask(task2);
        assertEquals(2, manager.getHistory().size(), "both task should be added");
        manager.addTask(task2);
        assertEquals(2, manager.getHistory().size(), "task shouldn't be added to the history twice ");
    }

    @Test
    @DisplayName("Порядок в истории при удалении задачи")
    public void testOrderInHistoryManagers() {
        Task task1 = new Task(1, "Test 1", "Description 1", Status.NEW);
        manager.addTask(task1);
        Task task2 = new Task(2, "Test 2", "Description 2", Status.NEW);
        manager.addTask(task2);
        Epic epic = new Epic(3, "Test 3", "Description 3");
        manager.addTask(epic);
        Task task3 = new Task(4, "Test 4", "Description 4", Status.NEW);
        manager.addTask(task3);
        Task task4 = new Task(5, "Test 5", "Description 5", Status.NEW);
        manager.addTask(task4);
        manager.remove(epic.getId());
        assertEquals(task1, manager.getHistory().getFirst(), "After remove middle the save order is incorrect");
        manager.remove(task1.getId());
        assertEquals(task2, manager.getHistory().getFirst(), "After remove first the save order is incorrect");
        manager.remove(task4.getId());
        assertEquals(task2, manager.getHistory().getFirst(), "After remove last the save order is incorrect");
    }
}