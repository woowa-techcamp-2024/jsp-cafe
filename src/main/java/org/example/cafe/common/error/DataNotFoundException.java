package org.example.cafe.common.error;

import jakarta.servlet.http.HttpServletResponse;

public class DataNotFoundException extends CafeException {

    public DataNotFoundException(String message) {
        super(HttpServletResponse.SC_NOT_FOUND, message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
