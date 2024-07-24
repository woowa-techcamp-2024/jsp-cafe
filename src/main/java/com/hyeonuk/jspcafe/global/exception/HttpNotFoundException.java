package com.hyeonuk.jspcafe.global.exception;

public class HttpNotFoundException extends RuntimeException{
    public HttpNotFoundException(String message) {
        super(message);
    }
}
