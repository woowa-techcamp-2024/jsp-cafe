package org.example.member.model.dto;

import org.example.member.model.dao.User;

public class UserResponseDto {

    private String userId;
    private String name;
    private String email;

    public static UserResponseDto createUserResponseDto(String userId, String name, String email) {
        UserResponseDto user = new UserResponseDto();
        user.userId = userId;
        user.name = name;
        user.email = email;

        return user;
    }

    public static UserResponseDto toResponse(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.userId = user.getUserId();
        userResponseDto.name = user.getName();
        userResponseDto.email = user.getEmail();
        return userResponseDto;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
