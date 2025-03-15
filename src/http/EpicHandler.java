package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Epic;
import task.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    EpicHandler(TaskManager taskManager, Gson gson) {
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
                if (pathParts.length == 2 && pathParts[1].equals("epics")) {
                    getTasksHandle(exchange, gson);
                } else if (pathParts.length == 3 && pathParts[1].equals("epics")) {
                    getTaskHandle(exchange, gson, pathParts);
                } else if (pathParts.length == 4 && pathParts[1].equals("epics") && pathParts[3].equals("subtasks")) {
                    getSubtasksHandle(exchange, gson, pathParts);
                } else {
                    sendNotFound(exchange, "Method not found");
                }
                break;
            case "POST":
                if (pathParts.length == 2 && pathParts[1].equals("epics")) {
                    postTaskHandle(exchange, gson);
                } else {
                    sendNotFound(exchange, "Method not found");
                }
                break;
            case "DELETE":
                if (pathParts.length == 3 && pathParts[1].equals("epics")) {
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
            List<Epic> tasks = taskManager.getEpics();
            String text = gson.toJson(tasks);
            sendText(exchange, text);
        } catch (Exception exp) {
            sendNotFound(exchange, "An error occurred during the request" + exp.getMessage());
        }
    }

    private void getSubtasksHandle(HttpExchange exchange, Gson gson, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Epic task = taskManager.getEpic(id);
            List<Subtask> tasks = taskManager.getEpicSubtask(id);
            String text = gson.toJson(tasks);
            sendText(exchange, text);
        } catch (Exception e) {
            sendNotFound(exchange, "An error occurred during the request" + e.getMessage());
        }
    }

    private void getTaskHandle(HttpExchange exchange, Gson gson, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Epic task = taskManager.getEpic(id);
            sendText(exchange, gson.toJson(task));
        } catch (Exception e) {
            sendNotFound(exchange, "Epic Id" + pathParts[2] + "not found");
        }
    }

    private void postTaskHandle(HttpExchange exchange, Gson gson) throws IOException {
        try {
            InputStream bodyInputStream = exchange.getRequestBody();
            String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
            Epic taskDeserialized = gson.fromJson(body, Epic.class);
            if (taskDeserialized == null) {
                sendNotFound(exchange, "Invalid format");
                return;
            }
            if (taskDeserialized.getId() == 0) {
                taskManager.addNewEpic(taskDeserialized);
                sendSuccessWithoutBody(exchange);
            } else {
                if (taskManager.getEpic(taskDeserialized.getId()) == null) {
                    sendNotFound(exchange, "Epic Id not found" + taskDeserialized.getId());
                } else {
                    taskManager.updateEpic(taskDeserialized);
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
            Epic task = taskManager.getEpic(id);
            taskManager.removeEpicForId(task.getId());
            sendText(exchange, "Epic was successfully deleted");
        } catch (Exception e) {
            sendNotFound(exchange, "Epic Id" + pathParts[2] + "not founded");
        }
    }
}