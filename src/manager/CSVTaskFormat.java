package manager;

import task.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class CSVTaskFormat {

    public static String toString(Task task) {
        if (task.getType() != TaskType.SUBTASK) {
            return (task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + ","
                    + task.getDescription() + "," + task.getStartTime() + "," + task.getDuration().toMinutes());
        } else {
            Subtask subtask = (Subtask) task;
            return subtask.getId() + "," + subtask.getType() + "," + subtask.getName() + "," + subtask.getStatus() + ","
                    + subtask.getDescription() + "," + subtask.getStartTime() + "," + subtask.getDuration().toMinutes()
                    + "," + subtask.getEpicId();
        }
    }

    public static Task taskFromString(String value) {
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TaskType type = TaskType.valueOf(values[1]);
        final String name = values[2];
        final Status status = Status.valueOf(values[3]);
        final String description = values[4];
        final LocalDateTime startTime = LocalDateTime.parse(values[5]);
        final Duration duration = Duration.ofMinutes(Long.parseLong(values[6]));
        if (TaskType.SUBTASK == type) {
            final int epicId = Integer.parseInt(values[7]);
            return new Subtask(id, name, description, status, startTime, duration, epicId);
        }
        if (type == TaskType.TASK) {
            return new Task(id, name, description, status, startTime, duration);
        } else {
            Epic epic = new Epic(id, name, description);
            epic.setStatus(status);
            epic.setDuration(duration);
            epic.setStartTime(startTime);
            return epic;
        }
    }

}
