package org.example.cafe.common.error;

public class DataNotFoundException extends CafeException {

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
