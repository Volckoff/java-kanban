package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    SubtaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        String requestPath = exchange.getRequestURI().getPath();
        String[] pathParts = requestPath.split("/");
        switch (requestMethod) {
            case "GET":
                if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
                    getTasksHandle(exchange, gson);
                } else if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
                    getTaskHandle(exchange, gson, pathParts);
                } else {
                    sendNotFound(exchange, "Method not found");
                }
                break;
            case "POST":
                if (pathParts.length == 2 && pathParts[1].equals("subtasks")) {
                    postTaskHandle(exchange, gson);
                } else {
                    sendNotFound(exchange, "Method not found");
                }
                break;
            case "DELETE":
                if (pathParts.length == 3 && pathParts[1].equals("subtasks")) {
                    deleteTaskHandle(exchange, pathParts);
                } else {
                    sendNotFound(exchange, "Method not found");
                }
                break;
            default:
                sendNotFound(exchange, "Method not found");
        }
    }

    private void getTasksHandle(HttpExchange exchange, Gson gson) throws IOException {
        try {
            List<Subtask> tasks = taskManager.getSubtasks();
            String text = gson.toJson(tasks);
            sendText(exchange, text);
        } catch (Exception exp) {
            sendNotFound(exchange, "An error occurred during the request" + exp.getMessage());
        }
    }

    private void getTaskHandle(HttpExchange exchange, Gson gson, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Subtask task = taskManager.getSubtask(id);
            sendText(exchange, gson.toJson(task));
        } catch (Exception e) {
            sendNotFound(exchange, "Subtask Id" + pathParts[2] + "not found");
        }
    }

    private void postTaskHandle(HttpExchange exchange, Gson gson) throws IOException {
        try {
            InputStream bodyInputStream = exchange.getRequestBody();
            String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
            Subtask taskDeserialized = gson.fromJson(body, Subtask.class);
            if (taskDeserialized == null) {
                sendNotFound(exchange, "Invalid format");
                return;
            }
            if (taskDeserialized.getId() == 0) {
                int id = taskManager.addNewSubtask(taskDeserialized);
                if (id == 0) {
                    sendNotFound(exchange, "Can't create Subtask");
                } else {
                    sendSuccessWithoutBody(exchange);
                }
            } else {
                if (taskManager.getSubtask(taskDeserialized.getId()) == null) {
                    sendNotFound(exchange, "Subtask Id not found" + taskDeserialized.getId());
                } else {
                    taskManager.updateSubtask(taskDeserialized);
                    sendSuccessWithoutBody(exchange);
                }
            }
        } catch (Exception exp) {
            sendNotFound(exchange, "An error occurred during the request" + exp.getMessage());
        }
    }

    private void deleteTaskHandle(HttpExchange exchange, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Subtask task = taskManager.getSubtask(id);
            taskManager.removeSubtaskForId(task.getId());
            sendText(exchange, "Task was successfully deleted");
        } catch (Exception e) {
            sendNotFound(exchange, "Subtask Id" + pathParts[2] + "not founded");
        }
    }
}