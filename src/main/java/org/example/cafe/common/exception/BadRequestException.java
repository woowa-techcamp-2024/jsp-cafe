package org.example.cafe.common.exception;

import jakarta.servlet.http.HttpServletResponse;

public class BadRequestException extends CafeException {

    public BadRequestException(String message) {
        super(HttpServletResponse.SC_BAD_REQUEST, message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
