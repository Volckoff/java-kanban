package httpTaksManager;

import com.google.gson.*;
import exceptions.NotFoundException;
import httpTaskServer.HttpTaskServer;
import httpTaskServer.TaskListTypeToken;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpEpicTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer;

    {
        try {
            taskServer = new HttpTaskServer(manager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Gson gson = HttpTaskServer.getGson();


    @BeforeEach
    public void setUp() {
        manager.clearAllTasks();
        manager.clearAllEpics();
        manager.clearAllSubtasks();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void addEpic() throws IOException, InterruptedException {

        Epic task = new Epic("Epic 1", "Description 1");
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request =
                HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Epic> tasksFromManager = manager.getEpics();
        assertNotNull(tasksFromManager, "Epic is empty");
        assertEquals(1, tasksFromManager.size(), "Wrong number of Epic");
        assertEquals("Epic 1", tasksFromManager.get(0).getName(), "Wrong name");
    }

    @Test
    public void deleteEpic() throws IOException, InterruptedException {

        Epic task = new Epic("Test 2", "Testing task 2");
        final int taskId = manager.addNewEpic(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getEpics().size(), "Epic should be deleted");
    }

    @Test
    public void getEpicById() throws IOException, InterruptedException {

        Epic task = new Epic("Epic 1", "Description 1");
        final int taskId = manager.addNewEpic(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject(), "Wrong response");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Epic responseTask = gson.fromJson(jsonObject, Epic.class);
        assertNotNull(responseTask, "Epic is empty");
        assertEquals("Epic 1", responseTask.getName(), "Wrong name");
    }

    @Test
    public void getEpics() throws IOException, InterruptedException {

        Epic task = new Epic("Epic 1", "Description 1");
        manager.addNewEpic(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray(), "Wrong response");
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Task> tasks = gson.fromJson(jsonArray, new TaskListTypeToken().getType());
        assertNotNull(tasks, "Epic is empty");
        assertEquals(1, tasks.size(), "Wrong number of Epic");
        assertEquals("Epic 1", tasks.get(0).getName(), "Wrong name");
    }

    @Test
    public void getEpicSubtasks() throws IOException, InterruptedException, NotFoundException {

        Epic epic = new Epic("Epic 1", "Description 1");
        final int epicId = manager.addNewEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 1", Status.NEW, LocalDateTime.now(),
                Duration.ofMinutes(5), epicId);
        manager.addNewSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId + "/subtasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray(), "Wrong response");
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Task> epics = gson.fromJson(jsonArray, new TaskListTypeToken().getType());
        assertNotNull(epics, "Epics is empty");
        assertEquals(1, epics.size(), "Wrong number of epic");
        assertEquals("Subtask 1", epics.get(0).getName(), "Wrong name");
    }
}
