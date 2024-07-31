package com.wootecam.jspcafe.exception;

import jakarta.servlet.http.HttpServletResponse;

public class NotFoundException extends CommonException {

    public NotFoundException(final String message) {
        super(message, HttpServletResponse.SC_NOT_FOUND);
    }
}
