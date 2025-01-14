import Manager.*;
import Task.*;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();



        //Создание
        System.out.println("Добавляем задачи:");
        Task task1 = new Task("Task#1", "Вызов службы перевозки", Status.NEW);
        final int taskId1 = manager.addNewTask(task1);
        Task task2 = new Task("Task#2", "Вызов такси", Status.IN_PROGRESS);
        final int taskId2 = manager.addNewTask(task2);
        System.out.println(manager.getTasks());
        System.out.println("Добавляем эпики:");
        Epic epic1 = new Epic("Task.Epic#1", "Переезд");
        final int epic1Id = manager.addNewEpic(epic1);
        Epic epic2 = new Epic("Task.Epic#2", "Уборка");
        final int epic2Id = manager.addNewEpic(epic2);
        System.out.println(manager.getEpics());
        System.out.println("Добавляем подзадачи:");
        Subtask subtask1 = new Subtask("Task.Subtask 1-1", "description1-1", Status.DONE, epic1Id);
        final int subtask1Id = manager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Task.Subtask 2-1", "description2-1", Status.DONE, epic1Id);
        final  int subtask2Id = manager.addNewSubtask(subtask2);
        Subtask subtask3 = new Subtask("Task.Subtask 3-2", "description3-2", Status.DONE, epic2Id);
        final int subtask3Id = manager.addNewSubtask(subtask3);
        System.out.println(manager.getSubtasks());
        System.out.println("Просмотр истории:");
        System.out.println(manager.getHistory());
        System.out.println("Сабтаски для Epic2:");
        System.out.println(manager.getEpicSubtask(epic2Id));
        System.out.println("Сабтаски для Epic1");
        System.out.println(manager.getEpicSubtask(epic1Id));
        System.out.println("Удаляем задачу task 1 и epic 2-1");
        manager.removeTaskForId(taskId1);
        manager.removeSubtaskForId(subtask2Id);
        System.out.println("Удаляем все задачи");
        manager.clearAllTasks();
        System.out.println("Удаляем все эпики");
        manager.clearAllEpics();
        System.out.println("Задачи:");
        System.out.println(manager.getTasks());
        System.out.println("Эпики");
        System.out.println(manager.getEpics());
        System.out.println("Подзадачи");
        System.out.println(manager.getSubtasks());

        // Проверка HistoryManager
//        Task task1 = new Task("Task#1", "Вызов службы перевозки", Status.NEW);
//        final int taskId1 = manager.addNewTask(task1);
//        Task task2 = new Task("Task#2", "Вызов службы перевозки", Status.NEW);
//        final int taskId2 = manager.addNewTask(task2);
//        Epic epic3 = new Epic("Epic#3", "Вызов службы перевозки");
//        final int epicId3 = manager.addNewEpic(epic3);
//        Subtask subtask4 = new Subtask("Subtask#4", "Вызов службы перевозки", Status.NEW, epicId3);
//        final int subtaskId4 = manager.addNewSubtask(subtask4);
//        Task task5 = new Task("Task#5", "Вызов службы перевозки", Status.NEW);
//        final int taskId5 = manager.addNewTask(task5);
//        Task task6 = new Task("Task#6", "Вызов службы перевозки", Status.NEW);
//        final int taskId6 = manager.addNewTask(task6);
//        Task task7 = new Task("Task#7", "Вызов службы перевозки", Status.NEW);
//        final int taskId7 = manager.addNewTask(task7);
//        Task task8 = new Task("Task#8", "Вызов службы перевозки", Status.NEW);
//        final int taskId8 = manager.addNewTask(task8);
//        Task task9 = new Task("Task#9", "Вызов службы перевозки", Status.NEW);
//        final int taskId9 = manager.addNewTask(task9);
//        Task task10 = new Task("Task#10", "Вызов службы перевозки", Status.NEW);
//        final int taskId10 = manager.addNewTask(task10);
//        Task task11 = new Task("Task#10", "Вызов службы перевозки", Status.NEW);
//        final int taskId11 = manager.addNewTask(task11);
//        manager.getTask(taskId1);
//        manager.getTask(taskId2);
//        manager.getEpic(epicId3);
//        manager.getSubtask(subtaskId4);
//        manager.getTask(taskId5);
//        manager.getTask(taskId6);
//        manager.getTask(taskId7);
//        manager.getTask(taskId8);
//        manager.getTask(taskId9);
//        manager.getTask(taskId10);
//        manager.getTask(taskId11);
//        System.out.println(manager.getHistory());
    }
}
