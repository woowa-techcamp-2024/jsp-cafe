package com.hyeonuk.jspcafe.global.exception;

public class HttpInternalServerErrorException extends RuntimeException{
    public HttpInternalServerErrorException(String message) {
        super(message);
    }
}
