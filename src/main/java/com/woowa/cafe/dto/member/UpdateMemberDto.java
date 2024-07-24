package com.woowa.cafe.dto.member;

import com.woowa.cafe.domain.Member;

import java.util.Map;

public record UpdateMemberDto(String lastPassword, String newPassword, String name, String email) {
    public static UpdateMemberDto from(final Map<String, String> params) {
        return new UpdateMemberDto(
                params.get("lastPassword"),
                params.get("newPassword"),
                params.get("name"),
                params.get("email")
        );
    }

    public Member toMember(final String memberId) {
        return new Member(memberId, newPassword(), name(), email());
    }
}
