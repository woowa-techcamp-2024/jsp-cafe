package com.codesquad.cafe.model.dto;

public class ErrorResponse {

    private String message;

    public ErrorResponse(){}

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
