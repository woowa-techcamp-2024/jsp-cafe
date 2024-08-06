package org.example.cafe.common.exception;

import jakarta.servlet.http.HttpServletResponse;

public class BadAuthenticationException extends CafeException {

    public BadAuthenticationException(String message) {
        super(HttpServletResponse.SC_FORBIDDEN, message);
    }

    public BadAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
