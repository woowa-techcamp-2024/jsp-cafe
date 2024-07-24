package com.wootecam.jspcafe.model;

public class User {

    private Long id;
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

    public User(final Long id, final String userId, final String password, final String name, final String email) {
        this(userId, password, name, email);
        this.id = id;
    }

    private void validate(final String userId, final String password, final String name, final String email) {
        if (userId == null || password == null || name == null || email == null
                || userId.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException("회원가입 시 모든 정보를 입력해야 합니다.");
        }
    }

    public Long getId() {
        return id;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
