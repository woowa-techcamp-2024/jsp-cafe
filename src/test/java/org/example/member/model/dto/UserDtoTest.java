package org.example.member.model.dto;

import org.example.member.model.dao.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoTest {

    @Test
    @DisplayName("createUserResponseDto 메서드로 UserDto 객체 생성 성공")
    void createUserResponseDto_shouldSucceed() {
        UserDto userDto = UserDto.createUserResponseDto("testUser", "Test Name", "test@example.com");

        assertNotNull(userDto);
        assertEquals("testUser", userDto.getUserId());
        assertEquals("Test Name", userDto.getName());
        assertEquals("test@example.com", userDto.getEmail());
    }

    @Test
    @DisplayName("User 객체로부터 UserDto 객체 생성 성공")
    void toResponse_shouldSucceed() {
        User user = User.createUser("testUser", "password123", "Test Name", "test@example.com");
        UserDto userDto = UserDto.toResponse(user);

        assertNotNull(userDto);
        assertEquals("testUser", userDto.getUserId());
        assertEquals("Test Name", userDto.getName());
        assertEquals("test@example.com", userDto.getEmail());
    }
}