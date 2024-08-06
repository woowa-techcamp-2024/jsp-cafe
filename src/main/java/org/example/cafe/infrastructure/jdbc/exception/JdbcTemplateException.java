package org.example.cafe.infrastructure.jdbc.exception;

public class JdbcTemplateException extends RuntimeException {

    public JdbcTemplateException(String message) {
        super(message);
    }

    public JdbcTemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
