package org.example.jspcafe.user.model;

public class User {
    private Long userId;
    private Nickname nickname;
    private Email email;
    private Password password;

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

    public User(Long userId, String nickname, String email, String password) {
        this.userId = userId;
        this.nickname = new Nickname(nickname);
        this.email = new Email(email);
        this.password = new Password(password);
    }
}
