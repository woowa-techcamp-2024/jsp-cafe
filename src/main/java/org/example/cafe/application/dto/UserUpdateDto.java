package org.example.cafe.application.dto;

import org.example.cafe.domain.User;

public record UserUpdateDto(String checkPassword,
                            String newPassword,
                            String nickname,
                            String email) {

    public User toUser(String userId) {
        return new User(userId,
                newPassword,
                nickname,
                email);
    }
}
