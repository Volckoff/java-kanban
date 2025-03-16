package httpTaksManager;

import adapters.DurationTypeAdapter;
import adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializedTest {

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .setPrettyPrinting()
            .create();

    @Test
    public void serializationTest() {
        Task task = new Task("Test 1", "Description 1", Status.IN_PROGRESS,
                LocalDateTime.now(), Duration.ofMinutes(5));
        String jsonTask = gson.toJson(task);
        Task task2 = gson.fromJson(jsonTask, Task.class);
        assertEquals(task, task2, "Serialization - deserialization is successful");
    }
}
