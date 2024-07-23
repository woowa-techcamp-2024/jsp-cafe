package org.example.member.model.dto;

import org.example.member.model.dao.User;

public class UserDto {

    private String userId;
    private String name;
    private String email;

    public static UserDto createUser(String userId, String name, String email) {
        UserDto user = new UserDto();
        user.userId = userId;
        user.name = name;
        user.email = email;

        return user;
    }

    public static UserDto toDto(User user) {
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
}
