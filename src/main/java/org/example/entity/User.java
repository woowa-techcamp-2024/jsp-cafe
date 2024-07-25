package org.example.entity;

public class User {
    private String userId;
    private String password;
    private String email;
    private String nickname;

    public User(String userId,String password, String email, String nickname) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public String getUserId() {
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void update(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }
}
