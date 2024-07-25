package com.woowa.hyeonsik.server.database;

public class JdbcException extends RuntimeException {
    public JdbcException(String message) {
        super(message);
    }

    public JdbcException(String message, Throwable e) {
        super(message, e);
    }

    public JdbcException(Throwable e) {
        super(e);
    }
}
