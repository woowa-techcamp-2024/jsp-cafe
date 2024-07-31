package org.example.exception;

public class NotSameAuthorException extends RuntimeException {

    public NotSameAuthorException(String s) {
        super(s);
    }
}
