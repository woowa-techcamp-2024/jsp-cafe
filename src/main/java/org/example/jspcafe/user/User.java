package org.example.jspcafe.user;

import java.util.Objects;

public class User {
    private Long id;
    private String userId;
    private String password;
    private String nickname;
    private String email;


    public User(Long id, String userId, String email, String nickname, String password) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public User(Long id, String email, String nickname, String password) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public User(String userId, String password, String nickname, String email) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(userId, user.userId) &&
                Objects.equals(password, user.password) &&
                Objects.equals(nickname, user.nickname) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, password, nickname, email);
    }
}
