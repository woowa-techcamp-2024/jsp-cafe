package org.example.cafe.common.error;

public class CafeException extends RuntimeException {

    public CafeException(String message) {
        super(message);
    }

    public CafeException(String message, Throwable cause) {
        super(message, cause);
    }
}
