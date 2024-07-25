package com.hyeonuk.jspcafe.global.exception;

public class InvalidMemberRegistRequest extends RuntimeException {
    public InvalidMemberRegistRequest(String message) {
        super(message);
    }
}
