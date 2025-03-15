package httpTaksManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import httpTaskServer.HttpTaskServer;
import httpTaskServer.TaskListTypeToken;
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

public class HttpPrioritizedTest {


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
    public void getPrioritized() throws IOException, InterruptedException {

        Task task = new Task("Task 1", "Description 1",
                Status.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        manager.addNewTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        assertTrue(jsonElement.isJsonArray(), "Wrong response");
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Task> tasks = gson.fromJson(jsonArray, new TaskListTypeToken().getType());
        assertNotNull(tasks, "Prioritized is empty");
        assertEquals(1, tasks.size(), "Wrong number of tasks");
        assertEquals("Task 1", tasks.get(0).getName(), "Wrong name");
    }
}
