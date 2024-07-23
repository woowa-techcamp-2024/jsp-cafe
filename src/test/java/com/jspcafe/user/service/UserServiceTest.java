package com.jspcafe.user.service;

import com.jspcafe.user.model.User;
import com.jspcafe.user.model.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
        userService = new UserService(userDao);
    }

    @Test
    void 유저를_정상적으로_저장한다() {
        // Given
        String email = "woowa@woowa.in";
        String nickname = "김배달";
        String password = "1234";

        // When
        userService.signUp(email, nickname, password);

        // Then
        User user = User.create("woowa@woowa.in", "김배달", "1234");
        User storedUser = userDao.findByEmail("woowa@woowa.in");
        assertNotNull(storedUser);
        assertEquals(user.email(), email);
        assertEquals(user.nickname(), nickname);
        assertTrue(user.verifyPassword(password));
    }
}