package org.example.member.model.dao;

public class Member {

    private String userId;
    private String password;
    private String name;
    private String email;

    public static Member createUser(String userId, String password, String name, String email) {
        Member member = new Member();
        member.userId = userId;
        member.password = password;
        member.name = name;
        member.email = email;

        return member;
    }

    public String getUserId() {
        return userId;
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

    private void validate() {

    }

    @Override
    public String toString() {
        return "Member{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
