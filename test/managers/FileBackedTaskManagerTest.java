package managers;

import exceptions.ManagerSaveException;
import manager.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    File file;

    @BeforeEach
    public void beforeEach() {
        try {
            file = File.createTempFile("temp", ".csv");
            taskManager = new FileBackedTaskManager(file);
        } catch (IOException ignored) {

        }
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

    @AfterEach
    public void afterEach() {
        if (file != null) {
            file.delete();
        }
    }

    @Test
    @DisplayName("Инициализация и загрузка из пустого файла")
    void testSaveAndLoadFromEmptyFile() throws IOException {
        File tempFile = File.createTempFile("temp", "csv");
        FileBackedTaskManager restoredManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertNotNull(taskManager);
        assertNotNull(restoredManager);
        assertTrue(Files.exists(restoredManager.getFile().toPath()));
    }

    @Test
    @DisplayName("Сохранение и загрузка нескольких задач")
    void saveAndLoadFromFileTest() throws IOException {
        File file = File.createTempFile("tempFile", ".csv");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        Task task = new Task("Task 1", "Description 1",
                Status.NEW, LocalDateTime.of(2025, 1, 1, 1, 1),
                Duration.ofMinutes(30));
        taskManager.addNewTask(task);
        Task oneMore = new Task("Task 1", "Description 1",
                Status.NEW, LocalDateTime.of(2025, 2, 2, 2, 2),
                Duration.ofMinutes(15));
        taskManager.addNewTask(oneMore);
        FileBackedTaskManager restoredFromFile = FileBackedTaskManager.loadFromFile(file);
        assertNotNull(restoredFromFile, "taskManagerFromFile is null!");
        assertEquals(restoredFromFile.getTasks().size(), taskManager.getTasks().size(),
                "The number of tasks in managers is different");
    }

    @Test
    public void testException() {
        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager manager = new FileBackedTaskManager(new File("/invalid/path/task.csv"));
            manager.save();
        }, "should result in an error");
    }

}