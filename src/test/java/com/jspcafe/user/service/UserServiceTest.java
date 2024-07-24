package com.jspcafe.user.service;

import com.jspcafe.exception.UserNotFoundException;
import com.jspcafe.test_util.H2Connector;
import com.jspcafe.test_util.H2Initializer;
import com.jspcafe.user.model.User;
import com.jspcafe.user.model.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private UserDao userDao;

    @BeforeEach
    void setUp() throws SQLException {
        H2Initializer.initializeDatabase(H2Connector.INSTANCE);
        userDao = new UserDao(H2Connector.INSTANCE);
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
        User storedUser = userDao.findByEmail("woowa@woowa.in")
                .orElseThrow(() -> new UserNotFoundException("email not found"));
        assertNotNull(storedUser);
        assertEquals(user.email(), email);
        assertEquals(user.nickname(), nickname);
        assertTrue(user.verifyPassword(password));
    }

    @Test
    void 모든유저의_정보를_가져온다() {
        // Given
        User user1 = User.create("woowa@woowa.in", "김배달", "1234");
        User user2 = User.create("coupang@co.pang", "이쿠팡", "4321");
        userDao.save(user1);
        userDao.save(user2);

        // When
        List<User> users = userService.findAll();

        // Then
        assertEquals(2, users.size());
    }

    @Test
    void id를_기준으로_유저의_정보를_가져온다() {
        // Given
        User user = User.create("woowa@woowa.in", "김배달", "1234");
        userDao.save(user);

        // When
        User storedUser = userService.findById(user.id());

        // Then
        assertEquals(user, storedUser);
    }

    @Test
    void 유저의_정보를_업데이트한다() {
        // Given
        User currentUser = User.create("woowa@woowa.in", "김배달", "1234");
        userDao.save(currentUser);

        // When
        userService.update(currentUser, "coupang@co.pang", "이쿠팡", "4321");

        // Then
        User updateUser = userService.findById(currentUser.id());
        assertEquals("coupang@co.pang", updateUser.email());
        assertEquals("이쿠팡", updateUser.nickname());
        assertTrue(updateUser.verifyPassword("4321"));
    }
}
