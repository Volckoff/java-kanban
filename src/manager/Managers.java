package manager;

import java.io.File;

public class Managers {

    private Managers() {

    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getDefaultFileBacked() {
        return new FileBackedTaskManager(new File("./resources/fileManager.csv"));
    }

}
