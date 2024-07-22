package org.example.jspcafe.user.model;

import org.example.jspcafe.PK;

public class User {
    @PK
    private Long userId;
    private Nickname nickname;
    private Email email;
    private Password password;
    
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

    public User(String nickname, String email, String password) {
        this.nickname = new Nickname(nickname);
        this.email = new Email(email);
        this.password = new Password(password);
    }
}
