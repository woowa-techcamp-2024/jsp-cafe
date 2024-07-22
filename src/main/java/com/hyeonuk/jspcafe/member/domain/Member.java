package com.hyeonuk.jspcafe.member.domain;

public class Member {
    private String id;
    private String password;
    private String nickname;
    private String email;

    public Member(String id, String password, String nickname, String email) {
        setId(id);
        setPassword(password);
        setNickname(nickname);
        setEmail(email);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
