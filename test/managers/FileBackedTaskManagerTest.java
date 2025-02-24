package managers;

import manager.FileBackedTaskManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager manager;


    @Test
    @DisplayName("Инициализация и загрузка из пустого файла")
    void testSaveAndLoadFromEmptyFile() throws IOException {
        File tempFile = File.createTempFile("temp", "csv");
        manager = new FileBackedTaskManager(tempFile);
        FileBackedTaskManager restoredManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertNotNull(manager);
        assertNotNull(restoredManager);
        assertTrue(Files.exists(manager.getFile().toPath()));
    }

    @Test
    @DisplayName("Сохранение и загрузка нескольких задач")
    void saveAndLoadFromFileTest() throws IOException {
        File tempFile = File.createTempFile("temp", "csv");
        manager = new FileBackedTaskManager(tempFile);
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW);
        manager.addNewTask(task1);
        Epic epic = new Epic("Epic 1", "Description 1");
        int epicId1 = manager.addNewEpic(epic);
        Subtask subtask1 = new Subtask(1, "Subtask 1", "Description 1", Status.NEW, epicId1);
        manager.addNewSubtask(subtask1);
        FileBackedTaskManager restoredManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertEquals(1, restoredManager.getTasks().size(), "Didn't save or load task");
        assertEquals(1, restoredManager.getEpics().size(), "Didn't save or load epic");
        assertEquals(1, restoredManager.getSubtasks().size(), "Didn't save or load subtask");
        assertEquals(manager.getTasks(), restoredManager.getTasks(), "the restored manager is different" +
                " from the original");
        assertEquals(manager.getEpics(), restoredManager.getEpics(), "the restored manager is different" +
                " from the original");
        assertEquals(manager.getSubtasks(), restoredManager.getSubtasks(), "the restored manager is" +
                " different from the original");
    }
}