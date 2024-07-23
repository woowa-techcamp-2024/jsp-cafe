package com.jspcafe.user.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
    }

    @Test
    void email로_유저정보를_찾을_수_있다() {
        // Given
        User user = User.create("woowa@woowa.in", "김배달", "1234");

        // When
        userDao.save(user);

        // Then
        User storedUser = userDao.findByEmail("woowa@woowa.in");
        assertNotNull(storedUser);
        assertEquals(user, storedUser);
    }

    @Test
    void id로_유저정보를_찾을_수_있다() {
        // Given
        User user = User.create("woowa@woowa.in", "김배달", "1234");

        // When
        userDao.save(user);

        // Then
        User storedUser = userDao.findById(user.id());
        assertNotNull(storedUser);
        assertEquals(user, storedUser);
    }
}
