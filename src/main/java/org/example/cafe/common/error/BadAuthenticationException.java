package org.example.cafe.common.error;

public class BadAuthenticationException extends CafeException {

    public BadAuthenticationException(String message) {
        super(message);
    }

    public BadAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
