package org.example.member.model.dto;

import org.example.member.model.dao.User;

public class UserDto {

    private String userId;
    private String name;
    private String email;

    public static UserDto createUserResponseDto(String userId, String name, String email) {
        UserDto user = new UserDto();
        user.userId = userId;
        user.name = name;
        user.email = email;

        return user;
    }

    public static UserDto toResponse(User user) {
        UserDto userDto = new UserDto();
        userDto.userId = user.getUserId();
        userDto.name = user.getName();
        userDto.email = user.getEmail();
        return userDto;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
