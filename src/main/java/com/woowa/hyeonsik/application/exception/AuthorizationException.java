package com.woowa.hyeonsik.application.exception;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message, Throwable c) {
        super(message, c);
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
