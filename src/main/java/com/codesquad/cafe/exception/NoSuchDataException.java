package com.codesquad.cafe.exception;

public class NoSuchDataException extends DBException{

    public NoSuchDataException() {
    }

    public NoSuchDataException(String message) {
        super(message);
    }

}
