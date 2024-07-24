package com.woowa.model;

public class User {
    private final String userId;
    private final String email;
    private final String password;
    private final String nickname;

    private User(String userId, String email, String password, String nickname) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public static User create(String userId, String email, String password, String nickname) {
        return new User(userId, email, password, nickname);
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }
}
