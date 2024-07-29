package cafe.service;

import cafe.domain.db.SessionDatabase;
import cafe.domain.db.UserDatabase;
import cafe.domain.entity.User;
import cafe.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserDatabase userDatabase;
    private SessionDatabase sessionDatabase;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userDatabase = Mockito.mock(UserDatabase.class);
        sessionDatabase = Mockito.mock(SessionDatabase.class);
        userService = new UserService(userDatabase, sessionDatabase);
    }

    @Test
    @DisplayName("ID로 사용자 조회")
    void find() {
        User user = User.of("1", "password", "홍길동", "hong@example.com");
        when(userDatabase.selectById("1")).thenReturn(user);

        UserDto userDto = userService.find("/users/1");

        assertNotNull(userDto);
        assertEquals("1", userDto.getId());
        assertEquals(user, userDto.getUser());
    }

    @Test
    @DisplayName("세션 ID로 사용자 조회")
    void findBySession() {
        User user = User.of("1", "password", "홍길동", "hong@example.com");
        when(sessionDatabase.selectById("session1")).thenReturn(user);

        Map<String, User> users = new HashMap<>();
        users.put("1", user);
        when(userDatabase.selectAll()).thenReturn(users);

        UserDto userDto = userService.findBySession("session1");

        assertNotNull(userDto);
        assertEquals("1", userDto.getId());
        assertEquals(user, userDto.getUser());
    }

    @Test
    @DisplayName("모든 사용자 조회")
    void findAll() {
        Map<String, User> users = new HashMap<>();
        users.put("1", User.of("1", "password", "홍길동", "hong@example.com"));
        when(userDatabase.selectAll()).thenReturn(users);

        Map<String, User> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("업데이트 중 사용자 찾을 수 없을 때 예외 발생")
    void updateUserNotFound() {
        when(userDatabase.selectById("1")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
                userService.update("/users/1", "홍길동 업데이트", "newpassword", "hong.updated@example.com", "password")
        );
    }

    @Test
    @DisplayName("업데이트 중 비밀번호가 틀렸을 때 예외 발생")
    void updateIncorrectPassword() {
        User existingUser = User.of("1", "password", "홍길동", "hong@example.com");
        when(userDatabase.selectById("1")).thenReturn(existingUser);

        assertThrows(IllegalArgumentException.class, () ->
                userService.update("/users/1", "홍길동 업데이트", "newpassword", "hong.updated@example.com", "wrongpassword")
        );
    }
}
