package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    TaskHandler(TaskManager taskManager, Gson gson) {
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
                if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
                    getTasksHandle(exchange, gson);
                } else if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
                    getTaskHandle(exchange, gson, pathParts);
                } else {
                    sendNotFound(exchange, "Method not found");
                }
                break;
            case "POST":
                if (pathParts.length == 2 && pathParts[1].equals("tasks")) {
                    postTaskHandle(exchange, gson);
                } else {
                    sendNotFound(exchange, "Method not found");
                }
                break;
            case "DELETE":
                if (pathParts.length == 3 && pathParts[1].equals("tasks")) {
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
            List<Task> tasks = taskManager.getTasks();
            String text = gson.toJson(tasks);
            sendText(exchange, text);
        } catch (Exception exp) {
            sendNotFound(exchange, "An error occurred during the request" + exp.getMessage());
        }
    }

    private void getTaskHandle(HttpExchange exchange, Gson gson, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Task task = taskManager.getTask(id);
            sendText(exchange, gson.toJson(task));
        } catch (Exception e) {
            sendNotFound(exchange, "Task Id " + pathParts[2] + " not Found");
        }
    }

    private void postTaskHandle(HttpExchange exchange, Gson gson) throws IOException {
        try {
            InputStream bodyInputStream = exchange.getRequestBody();
            String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
            Task taskDeserialized = gson.fromJson(body, Task.class);
            if (taskDeserialized == null) {
                sendNotFound(exchange, "Invalid format");
                return;
            }
            if (taskDeserialized.getId() == 0) {
                taskManager.addNewTask(taskDeserialized);
                sendSuccessWithoutBody(exchange);
            } else {
                if (taskManager.getTask(taskDeserialized.getId()) == null) {
                    sendNotFound(exchange, "Task Id not found " + taskDeserialized.getId());
                } else {
                    taskManager.updateTask(taskDeserialized);
                    sendSuccessWithoutBody(exchange);
                }
            }
        } catch (Exception exp) {
            sendNotFound(exchange, "An error occurred during the request " + exp.getMessage());
        }
    }

    private void deleteTaskHandle(HttpExchange exchange, String[] pathParts) throws IOException {
        try {
            int id = Integer.parseInt(pathParts[2]);
            Task task = taskManager.getTask(id);
            taskManager.removeTaskForId(task.getId());
            sendText(exchange, "Task was successfully deleted");
        } catch (Exception e) {
            sendNotFound(exchange, "Task Id " + pathParts[2] + "not founded");
        }
    }
}