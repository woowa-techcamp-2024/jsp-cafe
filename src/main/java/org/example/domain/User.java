package org.example.domain;

import java.time.LocalDateTime;

public class User {
    private Long userId;
    private String email;
    private String nickname;
    private String password;
    private LocalDateTime createdDt;

    public User(String email, String nickname, String password, LocalDateTime createdDt) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.createdDt = createdDt;
    }

    public User(Long userId, String email, String nickname, String password, LocalDateTime createdDt) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.createdDt = createdDt;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreatedDt() {
        return createdDt;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
