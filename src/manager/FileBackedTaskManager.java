package manager;

import exceptions.ManagerSaveException;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    private static final String headerString = ("id,type,name,status,description,epicId");

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        File file = new File("./resources/fileManager.csv");
        FileBackedTaskManager managerRestored = FileBackedTaskManager.loadFromFile(file);
        System.out.println("Состояние менеджера после создания");
        System.out.println("Задачи:");
        System.out.println(managerRestored.getTasks());
        System.out.println("Эпики:");
        System.out.println(managerRestored.getEpics());
        System.out.println("Подзадачи:");
        System.out.println(managerRestored.getSubtasks());
        System.out.println("______________________________________________________________");
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW);
        managerRestored.addNewTask(task1);
        Epic epic = new Epic("Epic 1", "Description 1");
        int epicId1 = managerRestored.addNewEpic(epic);
        Subtask subtask1 = new Subtask(1, "Subtask 1", "Description 1", Status.NEW, epicId1);
        managerRestored.addNewSubtask(subtask1);
        System.out.println("Состояние менеджера после добавления задач");
        System.out.println("Задачи:");
        System.out.println(managerRestored.getTasks());
        System.out.println("Эпики:");
        System.out.println(managerRestored.getEpics());
        System.out.println("Подзадачи:");
        System.out.println(managerRestored.getSubtasks());
    }

    protected void save() {
        if (!Files.exists(file.toPath())) {
            try {
                Files.createFile(file.toPath());
            } catch (Exception exp) {
                System.out.println("Не удалось создать файл");
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(headerString);
            writer.newLine();
            for (Task task : getTasks()) {
                writer.write(CSVTaskFormat.toString(task) + "\n");
            }
            for (Epic epic : getEpics()) {
                writer.write(CSVTaskFormat.toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasks()) {
                writer.write(CSVTaskFormat.toString(subtask) + "\n");
            }
        } catch (IOException exp) {
            throw new ManagerSaveException("Не удалось сохранить данные в файл.");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager managerRestored = new FileBackedTaskManager(file);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                Task task = CSVTaskFormat.taskFromString(line);
                if (task.getId() > managerRestored.idCounter) {
                    managerRestored.idCounter = task.getId();
                }
                switch (task.getType()) {
                    case TASK:
                        managerRestored.tasks.put(task.getId(), task);
                        break;
                    case SUBTASK:
                        managerRestored.subtasks.put(task.getId(), (Subtask) task);
                        break;
                    case EPIC:
                        managerRestored.epics.put(task.getId(), (Epic) task);
                        break;
                }
            }
            if (!managerRestored.subtasks.isEmpty()) {
                for (Subtask subtask : managerRestored.subtasks.values()) {
                    Epic epic = managerRestored.epics.get(subtask.getEpicId());
                    epic.getSubtasksId().add(subtask.getId());
                }
            }
        } catch (IOException exp) {
            throw new ManagerSaveException("Не удалось считать данные из файла.");
        }
        return managerRestored;
    }

    public File getFile() {
        return file;
    }

    @Override
    public int addNewTask(Task task) {
        super.addNewTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public void removeTaskForId(int id) {
        super.removeTaskForId(id);
        save();
    }

    @Override
    public void removeEpicForId(int id) {
        super.removeEpicForId(id);
        save();
    }

    @Override
    public void removeSubtaskForId(int id) {
        super.removeSubtaskForId(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        save();
    }
}
