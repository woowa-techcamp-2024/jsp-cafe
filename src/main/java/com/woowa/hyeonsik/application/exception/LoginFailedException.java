package com.woowa.hyeonsik.application.exception;

public class LoginFailedException extends AuthenticationException {
    public LoginFailedException(final String message, final Throwable c) {
        super(message, c);
    }

    public LoginFailedException(final String message) {
        super(message);
    }
}
