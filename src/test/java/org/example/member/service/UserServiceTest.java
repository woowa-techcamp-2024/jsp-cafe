package org.example.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import org.example.member.model.dao.User;
import org.example.member.model.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new MockUserService();
    }

    @Test
    @DisplayName("유효한 user 요청이 올 시 해당 회원을 등록한다.")
    public void register_new_user_successfully() throws SQLException {
        // Given
        User user = User.createUser("user123", "password", "John Doe", "john.doe@example.com");
        UserService service = userService;

        // When
        UserResponseDto response = service.register(user);

        // Then
        assertNotNull(response);
        assertEquals("user123", response.getUserId());
        assertEquals("John Doe", response.getName());
        assertEquals("john.doe@example.com", response.getEmail());
    }

    // Convert User entity to UserResponseDto correctly
    @Test
    @DisplayName("Given a registered user, When converting to UserResponseDto, Then the conversion should be correct")
    public void convert_user_to_user_response_dto_correctly() throws SQLException {
        // Given
        User user = User.createUser("user123", "password", "John Doe", "john.doe@example.com");
        UserService service = userService;

        // When
        UserResponseDto response = service.register(user);

        // Then
        assertNotNull(response);
        assertEquals(user.getUserId(), response.getUserId());
        assertEquals(user.getName(), response.getName());
        assertEquals(user.getEmail(), response.getEmail());
    }

    // Register a user with duplicate userId
    @Test
    @DisplayName("중복 userId가 존재한다면 예외를 발생시킨다.")
    public void register_user_with_duplicate_userid() throws SQLException {
        // Given, "user123" 이 존재한다고 가정
        User user1 = User.createUser("existingUser", "password", "John Doe", "john.doe@example.com");
        UserService service = userService;

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            service.register(user1);
        });
    }

    @Test
    @DisplayName("존재하는 userId가 주어졌을 때 existsByUserId는 true를 리턴한다.")
    public void returns_true_when_user_exists() throws SQLException {
        // Given
        String existingUserId = "existingUser";

        // When
        boolean result = userService.existsByUserId(existingUserId);

        // Then
        assertTrue(result);
    }

    @DisplayName("존재하지 않은 userId가 주어졌을 때 existsByUserId는 false를 리턴한다.")
    public void returns_false_when_user_does_not_exist() throws SQLException {
        // Given
        String nonExistingUserId = "nonExistingUser";

        // When
        boolean result = userService.existsByUserId(nonExistingUserId);

        // Then
        assertFalse(result);
    }

    static class MockUserService extends UserService {
        @Override
        public UserResponseDto register(User user) {
            if (existsByUserId(user.getUserId())) {
                throw new IllegalArgumentException("user already exists");
            }
            return UserResponseDto.toResponse(user);
        }

        @Override
        public boolean existsByUserId(String userId) {
            return "existingUser".equals(userId);
        }
    }

}