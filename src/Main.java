public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();


        //Создание
        System.out.println("Добавляем задачи:");
        Task task1 = new Task("Tusk#1", "Вызов службы перевозки", Status.NEW);
        final int taskId1 = manager.addNewTusk(task1);
        Task task2 = new Task("Tusk#2", "Вызов такси", Status.IN_PROGRESS);
        final int taskId2 = manager.addNewTusk(task2);
        System.out.println("Добавляем эпики:");
        Epic epic1 = new Epic("Epic#1", "Переезд", Status.NEW);
        final int epic1Id = manager.addNewEpic(epic1);
        Epic epic2 = new Epic("Epic#2", "Уборка", Status.DONE);
        final int epic2Id = manager.addNewEpic(epic2);
        System.out.println("Добавляем подзадачи:");
        Subtask subtask1 = new Subtask("Subtask 1-1", "description1-1", Status.DONE, epic1Id);
        final int subtask1Id = manager.addNewSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2-1", "description2-1", Status.NEW, epic1Id);
        final  int subtask2Id = manager.addNewSubtask(subtask2);
        Subtask subtask3 = new Subtask("Subtask 3-2", "description3-2", Status.DONE, epic2Id);
        final int subtask3Id = manager.addNewSubtask(subtask3);
        System.out.println("Сабтаски для Epic2:");
        System.out.println(manager.getEpicSubtask(epic2Id));
        System.out.println("Сабтаски для Epic1");
        System.out.println(manager.getEpicSubtask(epic1Id));
        System.out.println("Удаляем задачу task 1 и epic 2-1");
        manager.removeTaskForId(taskId1);
        manager.removeSubtaskForId(subtask2Id);
        manager.printAll();
        System.out.println("Удаляем все задачи");
        manager.clearAllTasks();
        System.out.println("Удаляем все эпики");
        manager.clearAllEpics();
        manager.printAll();



    }
}
