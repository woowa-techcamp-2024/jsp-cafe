package com.woowa.cafe.domain;

import java.util.Objects;

public class Member {

    private String memberId;
    private String password;
    private String name;
    private String email;

    public Member(final String memberId, final String password, final String name, final String email) {
        this.memberId = memberId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public boolean matchPassword(final String password) {
        return this.password.equals(password);
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId='" + memberId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Member member)) return false;
        return Objects.equals(memberId, member.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(memberId);
    }
}
