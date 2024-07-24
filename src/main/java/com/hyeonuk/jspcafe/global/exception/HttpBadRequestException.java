package com.hyeonuk.jspcafe.global.exception;

public class HttpBadRequestException extends RuntimeException{
    public HttpBadRequestException(String message) {
        super(message);
    }
}
