package managers;

import exceptions.ManagerSaveException;
import manager.FileBackedTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {


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

    @Override
    TaskManager initTaskManager() {
        try {
            File tempFile = File.createTempFile("temp", ".csv");
            return FileBackedTaskManager.loadFromFile(tempFile);
        } catch (IOException ignored) {
        }
        return null;
    }
}