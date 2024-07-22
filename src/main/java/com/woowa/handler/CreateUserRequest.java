package com.woowa.handler;

public record CreateUserRequest(String email, String password, String nickname) {
}
