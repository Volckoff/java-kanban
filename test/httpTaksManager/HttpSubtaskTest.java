package httpTaksManager;

import adapters.DurationTypeAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.*;
import http.HttpTaskServer;
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

public class HttpSubtaskTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer;

    {
        try {
            taskServer = new HttpTaskServer(manager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .setPrettyPrinting()
            .create();
    Epic epic = new Epic("Epic 1", "Description 1");


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
    public void addSubtask() throws IOException, InterruptedException {

        final int epicId = manager.addNewEpic(epic);
        Subtask task = new Subtask("Subtask 1", "Description 1",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5), epicId);
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Subtask> tasksFromManager = manager.getSubtasks();
        assertNotNull(tasksFromManager, "Subtask is empty");
        assertEquals(1, tasksFromManager.size(), "Wrong number of subtasks");
        assertEquals("Subtask 1", tasksFromManager.get(0).getName(), "Wrong name");
    }

    @Test
    public void deleteSubtask() throws IOException, InterruptedException {

        final int epicId = manager.addNewEpic(epic);
        Subtask task = new Subtask("Subtask 1", "Description 1",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5), epicId);
        final int taskId = manager.addNewSubtask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getSubtasks().size(), "Subtask should be deleted");
    }

    @Test
    public void getSubtaskById() throws IOException, InterruptedException {

        final int epicId = manager.addNewEpic(epic);
        Subtask task = new Subtask("Subtask 1", "Description 1",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5), epicId);
        final int taskId = manager.addNewSubtask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject(), "Wrong response");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Task responseTask = gson.fromJson(jsonObject, Subtask.class);
        assertNotNull(responseTask, "Subtask is empty");
        assertEquals("Subtask 1", responseTask.getName(), "Wrong name");
    }

    @Test
    public void getSubtasks() throws IOException, InterruptedException {

        final int epicId = manager.addNewEpic(epic);
        Subtask subtask = new Subtask("Subtask 1", "Description 1",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5), epicId);
        manager.addNewSubtask(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray(), "Wrong response");
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Task> subtasks = gson.fromJson(jsonArray, new TaskListTypeToken().getType());
        assertNotNull(subtasks, "Subtasks is empty");
        assertEquals(1, subtasks.size(), "Wrong number of subtasks");
        assertEquals("Subtask 1", subtasks.get(0).getName(), "Wrong name");
    }
}
