package com.hyeonuk.jspcafe.member.domain;

public class Member {
    private Long id;
    private String memberId;
    private String password;
    private String nickname;
    private String email;

    public Member(Long id, String memberId, String password, String nickname, String email) {
        setId(id);
        setMemberId(memberId);
        setPassword(password);
        setNickname(nickname);
        setEmail(email);
    }

    public Member(String memberId, String password, String nickname, String email){
        this(null, memberId,password,nickname,email);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
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
