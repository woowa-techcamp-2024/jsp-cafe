package com.codesquad.cafe.model;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String email;
    private LocalDateTime createdAt;

    public User() {
    }

    public User(Long id, String username, String password, String name, String email,
                LocalDateTime createdAt) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username은 필수 값입니다.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password 필수 값입니다.");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name 필수 값입니다.");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email 필수 값입니다.");
        }
        if (createdAt == null || createdAt.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("createdAt 는 현재시간 이전이야 합니다.");
        }
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }

    public static User of(String username, String password, String name, String email) {
        return new User(null, username, password, name, email, LocalDateTime.now());
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append(", id='").append(id).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", username='").append(username).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
