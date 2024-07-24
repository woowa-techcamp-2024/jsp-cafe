package com.woowa.cafe.dto.member;

import com.woowa.cafe.domain.Member;

import java.util.Map;

public record SaveMemberDto(String memberId, String password,
                            String name, String email) {

    public static SaveMemberDto from(final Map<String, String> body) {
        return new SaveMemberDto(body.get("memberId"),
                body.get("password"),
                body.get("name"),
                body.get("email"));
    }

    public Member toMember() {
        return new Member(memberId(), password(), name(), email());
    }

}
