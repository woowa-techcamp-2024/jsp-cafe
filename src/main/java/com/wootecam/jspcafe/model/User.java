package com.wootecam.jspcafe.model;

public class User {

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public User(final String userId, final String password, final String name, final String email) {
        validate(userId, password, name, email);
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    private void validate(final String userId, final String password, final String name, final String email) {
        if (userId == null || password == null || name == null || email == null
        || userId.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException("회원가입 시 모든 정보를 입력해야 합니다.");
        }
    }
}
