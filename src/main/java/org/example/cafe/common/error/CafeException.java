package org.example.cafe.common.error;

import jakarta.servlet.http.HttpServletResponse;

public class CafeException extends RuntimeException {
    protected final int statusCode;

    public CafeException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public CafeException(String message) {
        super(message);
        this.statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }

    public CafeException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
