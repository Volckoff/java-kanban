package manager;

import task.*;

public class CSVTaskFormat {

    public static String toString(Task task) {
        if (task.getType() != TaskType.SUBTASK) {
            return (task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + ","
                    + task.getDescription());
        } else {
            Subtask subtask = (Subtask) task;
            return subtask.getId() + "," + subtask.getType() + "," + subtask.getName() + "," + subtask.getStatus() + ","
                    + subtask.getDescription() + "," + subtask.getEpicId();
        }
    }

    public static Task taskFromString(String value) {
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        final String name = values[2];
        final Status status = Status.valueOf(values[3]);
        final String description = values[4];
        if (TaskType.SUBTASK == type) {
            final int epicId = Integer.parseInt(values[5]);
            return new Subtask(id, name, description, status, epicId);
        }
        if (type == TaskType.TASK) {
            return new Task(id, name, description, status);
        } else {
            return new Epic(id, name, description);
        }
    }

}
