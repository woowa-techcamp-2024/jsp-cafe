package com.woowa.cafe.exception;

public class HttpException extends IllegalArgumentException {

    private final HttpStatus status;

    public HttpException(final HttpStatus status, final String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

}
