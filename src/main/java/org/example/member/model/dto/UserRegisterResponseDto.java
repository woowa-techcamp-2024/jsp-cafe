package org.example.member.model.dto;

import org.example.member.model.dao.User;

public class UserRegisterResponseDto {

    private String userId;
    private String name;
    private String email;

    public static UserRegisterResponseDto createUserResponseDto(String userId, String name, String email) {
        UserRegisterResponseDto user = new UserRegisterResponseDto();
        user.userId = userId;
        user.name = name;
        user.email = email;

        return user;
    }

    public static UserRegisterResponseDto toResponse(User user) {
        UserRegisterResponseDto userRegisterResponseDto = new UserRegisterResponseDto();
        userRegisterResponseDto.userId = user.getUserId();
        userRegisterResponseDto.name = user.getName();
        userRegisterResponseDto.email = user.getEmail();
        return userRegisterResponseDto;
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
