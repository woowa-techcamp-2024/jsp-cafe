package org.example.dto;

import org.example.entity.User;

public record UserCreateReqDto(
    String userId,
    String password,
    String nickname,
    String email
) {

    public static UserCreateReqDto of(String userId, String password, String nickname, String email) {
        return new UserCreateReqDto(userId, password, nickname, email);
    }

    public User toEntity() {
        return new User(userId, password, email, nickname);
    }
}
