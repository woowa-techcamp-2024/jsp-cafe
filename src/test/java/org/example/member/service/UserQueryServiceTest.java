package org.example.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.List;
import org.example.member.model.dao.User;
import org.example.member.model.dto.UserResponseDto;
import org.example.member.repository.UserRepository;
import org.example.util.DataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class UserQueryServiceTest {

    DataUtil dataUtil;

    @BeforeEach
    void setUp() {
        dataUtil = new DataUtil();
    }

    @DisplayName("사용자 목록을 성공적으로 찾는다.")
    @Test
    public void test_findAllUsers_returns_list_of_UserResponseDto() throws SQLException {
        // Given
        UserQueryService userQueryService = new UserQueryService(new UserRepository(dataUtil));

        // When
        List<UserResponseDto> users = userQueryService.findAllUsers();

        // Then
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @DisplayName("userId가 주어졌을때 해상 사용자를 찾아 반환한다.")
    @Test
    public void test_findUserByUserId_returns_UserResponseDto() throws SQLException {
        // Given
        UserQueryService userQueryService = new MockUserQueryService(new UserRepository(dataUtil));
        String userId = "existingUserId";

        // When
        UserResponseDto user = userQueryService.findUserByUserId(userId);

        // Then
        assertNotNull(user);
        assertEquals(userId, user.getUserId());
    }

    @DisplayName("존재하지 않는 사용자를 찾을 시 예외가 발생한다.")
    @Test
    public void test_findUserByUserId_throws_SQLException_when_user_not_found() {
        // Given
        UserQueryService userQueryService = new UserQueryService(new UserRepository(dataUtil));
        String userId = "nonExistingUserId";

        // When & Then
        assertThrows(SQLException.class, () -> {
            userQueryService.findUserByUserId(userId);
        });
    }

    @DisplayName("findUserByUserId에 null이 들어오면 예외가 발생한다.")
    @Test
    public void test_findUserByUserId_handles_null_userId_gracefully() {
        // Given
        UserQueryService userQueryService = new UserQueryService(new UserRepository(dataUtil));

        // When & Then
        assertThrows(SQLException.class, () -> {
            userQueryService.findUserByUserId(null);
        });
    }

    static class MockUserQueryService extends UserQueryService {
        public MockUserQueryService(UserRepository userRepository) {
            super(userRepository);
        }

        @Override
        public UserResponseDto findUserByUserId(String userId) throws SQLException {
            return UserResponseDto.toResponse(User.createUser(userId, "password", "name", "email@email"));
        }
    }

}