package com.codesquad.cafe.exception;

public class DBException extends RuntimeException {

    public DBException() {
    }

    public DBException(String message) {
        super(message);
    }

}
