package codesquad.jspcafe.domain.user.domain;

import codesquad.jspcafe.domain.user.domain.values.Email;

public class User {

    private String userId;
    private String password;
    private String username;
    private Email email;

    public User(String userId, String password, String username, String email) {
        this.userId = verifyUserId(userId);
        this.password = verifyPassword(password);
        this.username = verifyUsername(username);
        this.email = Email.from(email);
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Email getEmail() {
        return email;
    }

    private String verifyUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId should not null");
        }
        return userId;
    }

    private String verifyPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password should not null");
        }
        return password;
    }

    private String verifyUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username should not null");
        }
        return username;
    }
}
