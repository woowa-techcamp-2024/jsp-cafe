package org.example.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
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

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.createUser("user1", "password", "John Doe", "john.doe@example.com");
    }

    @Test
    @DisplayName("새로운 사용자 등록 성공")
    void register_new_user_successfully() throws SQLException {
        when(userRepository.existsByUserId("user1")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserDto result = userService.register(testUser);

        assertNotNull(result);
        assertEquals("user1", result.getUserId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 ID로 사용자 정보 조회 성공")
    void retrieve_user_by_userid_successfully() throws SQLException {
        when(userRepository.findUserByUserId("user1")).thenReturn(testUser);

        UserDto result = userService.getUserFromUserId("user1");

        assertNotNull(result);
        assertEquals("user1", result.getUserId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
    }

    @Test
    @DisplayName("비밀번호 일치 시 사용자 정보 수정 성공")
    void edit_user_info_successfully_when_passwords_match() throws SQLException {
        User updatedUser = User.createUser("user1", "password", "John Smith", "john.smith@example.com");
        when(userRepository.findUserByUserId("user1")).thenReturn(testUser);
        when(userRepository.update(any(User.class))).thenReturn(updatedUser);

        UserDto result = userService.editUser("user1", updatedUser);

        assertNotNull(result);
        assertEquals("user1", result.getUserId());
        assertEquals("John Smith", result.getName());
        assertEquals("john.smith@example.com", result.getEmail());
    }

    @Test
    @DisplayName("사용자 인증 성공")
    void validate_user_credentials_successfully() throws SQLException {
        when(userRepository.findUserByUserId("user1")).thenReturn(testUser);

        boolean isValid = userService.validateUser("user1", "password");

        assertTrue(isValid);
    }

    @Test
    @DisplayName("사용자 ID 존재 여부 확인")
    void check_existence_of_user_by_userid() throws SQLException {
        when(userRepository.existsByUserId("user1")).thenReturn(true);

        boolean exists = userService.existsByUserId("user1");

        assertTrue(exists);
    }

    @Test
    @DisplayName("이미 존재하는 사용자 ID로 등록 시 예외 발생")
    void register_user_with_existing_userid() throws SQLException {
        when(userRepository.existsByUserId("user1")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.register(testUser));
    }
}