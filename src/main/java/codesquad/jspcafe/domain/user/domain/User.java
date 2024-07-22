package codesquad.jspcafe.domain.user.domain;

import codesquad.jspcafe.domain.user.domain.values.Email;

public class User {

    private String userId;
    private String password;
    private String username;
    private Email email;

    public User(String userId, String password, String username, String email) {
        this.userId = userId;
        this.password = password;
        this.username = username;
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
}
