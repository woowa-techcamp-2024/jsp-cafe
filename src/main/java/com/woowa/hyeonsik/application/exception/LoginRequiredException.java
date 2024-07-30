package com.woowa.hyeonsik.application.exception;

public class LoginRequiredException extends AuthenticationException {
    public LoginRequiredException(final String message, final Throwable c) {
        super(message, c);
    }

    public LoginRequiredException(final String message) {
        super(message);
    }
}
