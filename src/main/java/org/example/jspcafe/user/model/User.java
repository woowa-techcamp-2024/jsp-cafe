package org.example.jspcafe.user.model;

import org.example.jspcafe.PK;

import java.time.LocalDateTime;

public class User {
    @PK
    private Long userId;
    private Nickname nickname;
    private Email email;
    private Password password;
    private LocalDateTime createdAt;

    public void updatePassword(String password) {
        Password updatePassword = new Password(password);
        if (this.password.equals(updatePassword)) {
            throw new IllegalArgumentException("기존 비밀번호와 동일합니다.");
        }
        this.password = updatePassword;
    }
    public Long getUserId() {
        return userId;
    }

    public Nickname getNickname() {
        return nickname;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User(
            String nickname,
            String email,
            String password,
            LocalDateTime createdAt
    ) {
        this.nickname = new Nickname(nickname);
        this.email = new Email(email);
        this.password = new Password(password);
        validateCreatedAt(createdAt);
        this.createdAt = createdAt;
    }

    private void validateCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("생성일자는 필수입니다.");
        }
    }
}
