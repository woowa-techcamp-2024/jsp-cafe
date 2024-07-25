package com.hyeonuk.jspcafe.global.exception;

public class InvalidArticleRegistRequest extends RuntimeException{
    public InvalidArticleRegistRequest(String message) {
        super(message);
    }
}
