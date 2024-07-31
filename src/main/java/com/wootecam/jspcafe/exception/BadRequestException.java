package com.wootecam.jspcafe.exception;

import jakarta.servlet.http.HttpServletResponse;

public class BadRequestException extends CommonException {

    public BadRequestException(final String message) {
        super(message, HttpServletResponse.SC_BAD_REQUEST);
    }
}
