package exceptions;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(final String message) {
        super(message);
    }

    public ManagerSaveException(String message, IOException exp) {
        super(message);
    }

}
