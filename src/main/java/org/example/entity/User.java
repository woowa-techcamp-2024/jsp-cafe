package org.example.entity;

public class User {
    private Integer userId;
    private String password;
    private String email;
    private String nickname;

    public User(String password, String email, String nickname) {
        this.userId = null;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
