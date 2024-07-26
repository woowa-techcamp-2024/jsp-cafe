package com.codesquad.cafe.model;

import jakarta.servlet.annotation.WebServlet;

public class UserJoinRequest {
    private String username;
    private String password;
    private String name;
    private String email;

    public String getUsername() {
        return username;
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

    public User toUser() {
        return User.of(username, password, name, email);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserJoinRequest{");
        sb.append("username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
