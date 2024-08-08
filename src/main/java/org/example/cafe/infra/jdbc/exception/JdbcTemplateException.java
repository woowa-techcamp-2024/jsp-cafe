package org.example.cafe.infra.jdbc.exception;

public class JdbcTemplateException extends RuntimeException {

    public JdbcTemplateException(String s) {
        super(s);
    }

    public JdbcTemplateException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
