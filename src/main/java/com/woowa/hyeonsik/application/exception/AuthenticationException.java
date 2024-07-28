package com.woowa.hyeonsik.application.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message, Throwable c) {
        super(message, c);
    }

    public AuthenticationException(final String message) {
        super(message);
    }
}
