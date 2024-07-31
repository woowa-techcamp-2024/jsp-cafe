package com.wootecam.jspcafe.exception;

public class CommonException extends RuntimeException {

    private final String message;
    private final int statusCode;

    public CommonException(final String message, final int statusCode) {
        super(message);
        this.message = message;
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
