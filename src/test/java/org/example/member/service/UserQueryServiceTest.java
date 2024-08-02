package org.example.member.service;

import org.example.member.model.dao.User;
import org.example.member.model.dto.UserDto;
import org.example.member.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserQueryService userQueryService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = User.createUser("user1", "password1", "User One", "user1@example.com");
        user2 = User.createUser("user2", "password2", "User Two", "user2@example.com");
    }

    @Test
    @DisplayName("모든 사용자 조회 성공")
    void findAllUsers_shouldReturnAllUsers() throws SQLException {
        // Given
        when(userRepository.findAllUsers()).thenReturn(Arrays.asList(user1, user2));

        // When
        List<UserDto> result = userQueryService.findAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUserId());
        assertEquals("user2", result.get(1).getUserId());
        verify(userRepository).findAllUsers();
    }

    @Test
    @DisplayName("사용자 ID로 특정 사용자 조회 성공")
    void findUserByUserId_shouldReturnUserWhenExists() throws SQLException {
        // Given
        when(userRepository.findUserByUserId("user1")).thenReturn(user1);

        // When
        UserDto result = userQueryService.findUserByUserId("user1");

        // Then
        assertNotNull(result);
        assertEquals("user1", result.getUserId());
        assertEquals("User One", result.getName());
        assertEquals("user1@example.com", result.getEmail());
        verify(userRepository).findUserByUserId("user1");
    }

    @Test
    @DisplayName("존재하지 않는 사용자 ID로 조회 시 IllegalArgumentException 발생")
    void findUserByUserId_shouldReturnNullWhenUserDoesNotExist() throws SQLException {
        // Given
        when(userRepository.findUserByUserId("nonexistent")).thenReturn(null);

        // When
        // Then
        assertThrows(IllegalArgumentException.class, () -> userQueryService.findUserByUserId("nonexistent"));
        verify(userRepository).findUserByUserId("nonexistent");
    }

    @Test
    @DisplayName("사용자 조회 시 SQLException 발생")
    void findAllUsers_shouldThrowSQLException() throws SQLException {
        // Given
        when(userRepository.findAllUsers()).thenThrow(new SQLException("Database error"));

        // When & Then
        assertThrows(SQLException.class, () -> userQueryService.findAllUsers());
        verify(userRepository).findAllUsers();
    }

    @Test
    @DisplayName("특정 사용자 조회 시 SQLException 발생")
    void findUserByUserId_shouldThrowSQLException() throws SQLException {
        // Given
        when(userRepository.findUserByUserId("user1")).thenThrow(new SQLException("Database error"));

        // When & Then
        assertThrows(SQLException.class, () -> userQueryService.findUserByUserId("user1"));
        verify(userRepository).findUserByUserId("user1");
    }
}