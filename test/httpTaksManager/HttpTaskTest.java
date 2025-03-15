package httpTaksManager;

import com.google.gson.*;
import http.HttpTaskServer;
import http.TaskListTypeToken;
import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
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

public class HttpTaskTest {

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
    public void addTask() throws IOException, InterruptedException {

        Task task = new Task("Task 1", "Description 1",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request =
                HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = manager.getTasks();
        assertNotNull(tasksFromManager, "Task is Empty");
        assertEquals(1, tasksFromManager.size(), "Wrong number of Tasks");
        assertEquals("Task 1", tasksFromManager.get(0).getName(), "Wrong name");
    }

    @Test
    public void deleteTask() throws IOException, InterruptedException {

        Task task = new Task("Task 1", "Description 1",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        final int taskId = manager.addNewTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getTasks().size(), "Task should deleted");
    }

    @Test
    public void getTaskById() throws IOException, InterruptedException {

        Task task = new Task("Task 1", "Description 1",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        final int taskId = manager.addNewTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonObject(), "Wrong response");
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Task responseTask = gson.fromJson(jsonObject, Task.class);
        assertNotNull(responseTask, "Task is empty");
        assertEquals("Task 1", responseTask.getName(), "Wrong name");
    }

    @Test
    public void getTasks() throws IOException, InterruptedException {

        Task task = new Task("Task 1", "Description 1",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        manager.addNewTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray(), "Wrong response");
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Task> tasks = gson.fromJson(jsonArray, new TaskListTypeToken().getType());
        assertNotNull(tasks, "Task is empty");
        assertEquals(1, tasks.size(), "Wrong number of tasks");
        assertEquals("Task 1", tasks.get(0).getName(), "Wrong name");
    }

}
