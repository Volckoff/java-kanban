package httpTaskServer;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    PrioritizedHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");
        if (requestMethod.equals("GET") && pathParts.length == 2 && pathParts[1].equals("prioritized")) {
            try {
                List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
                String text = gson.toJson(prioritizedTasks);
                sendText(exchange, text);
            } catch (Exception exp) {
                sendNotFound(exchange, "Request Error" + exp.getMessage());
            }
        } else {
            sendNotFound(exchange, "Method not found");
        }
    }
}