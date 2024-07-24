package com.woowa.hyeonsik.domain;

import java.util.regex.Pattern;

public class User {
    private static final int MIN_PASSWORD_LENGTH = 2;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^([A-Za-z0-9+_.-]+@[A-Za-z0-9+_-]+[.][A-Za-z0-9+_-]+)$");

    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        validate();
    }

    private void validate() {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("UserId는 Null 또는 빈 값일 수 없습니다.");
        }
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("암호는 " + MIN_PASSWORD_LENGTH + "글자 이상이어야 합니다.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 Null 또는 빈 값일 수 없습니다.");
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("이메일이 규격에 맞지 않습니다. Email: " + email);
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
