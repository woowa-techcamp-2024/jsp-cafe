package com.woowa.cafe.exception;

public class HttpException extends IllegalArgumentException {

    private final int status;

    public HttpException(final int status, final String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
