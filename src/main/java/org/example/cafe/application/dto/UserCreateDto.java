package org.example.cafe.application.dto;

import org.example.cafe.domain.User;

public record UserCreateDto(String userId,
                            String password,
                            String nickname,
                            String email) {

    public User toUser() {
        return new User(userId,
                password,
                nickname,
                email);
    }
}
