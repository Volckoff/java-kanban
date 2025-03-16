package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");
        if (requestMethod.equals("GET") && pathParts.length == 2 && pathParts[1].equals("history")) {
            try {
                List<Task> history = taskManager.getHistory();
                String text = gson.toJson(history);
                sendText(exchange, text);
            } catch (Exception exp) {
                sendNotFound(400, exchange, "Request Error" + exp.getMessage());
            }
        } else {
            sendNotFound(405, exchange, "Method Not Allowed");
        }
    }
}