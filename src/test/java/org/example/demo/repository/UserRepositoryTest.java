package org.example.demo.repository;

import org.example.demo.domain.User;
import org.example.demo.model.UserCreateDao;
import org.example.demo.model.UserUpdateDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRepositoryTest extends RepositoryTestSupport {
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        setDb();
        userRepository = new UserRepository(dbConfig);
    }

    @AfterEach
    void tearDown() {
        cleanUp();
    }

    @Test
    void testAddAndGetUser() {
        // 사용자 추가
        UserCreateDao userCreateDao = new UserCreateDao("testUser", "password", "Test User", "test@example.com");
        userRepository.addUser(userCreateDao);

        // 사용자 조회
        Optional<User> user = userRepository.getUserByUserId("testUser");
        assertTrue(user.isPresent());
        assertEquals("Test User", user.get().getName());
        assertEquals("test@example.com", user.get().getEmail());
    }

    @Test
    void testGetUsers() {
        // 사용자 추가
        userRepository.addUser(new UserCreateDao("user1", "password", "User One", "user1@example.com"));
        userRepository.addUser(new UserCreateDao("user2", "password", "User Two", "user2@example.com"));

        // 사용자 목록 조회
        List<User> users = userRepository.getUsers();
        assertEquals(2, users.size());
    }

    @Test
    void testUpdateUser() {
        // 사용자 추가
        UserCreateDao userCreateDao = new UserCreateDao("testUser", "password", "Test User", "test@example.com");
        userRepository.addUser(userCreateDao);

        // 사용자 조회
        Optional<User> user = userRepository.getUserByUserId("testUser");
        assertTrue(user.isPresent());

        // 사용자 수정
        UserUpdateDao userUpdateDao = new UserUpdateDao(user.get().getId(), "newpassword", "Updated User", "updated@example.com");
        userRepository.updateUser(userUpdateDao);

        // 사용자 수정된 정보 조회
        Optional<User> updatedUser = userRepository.getUserByUserId("testUser");
        assertTrue(updatedUser.isPresent());
        assertEquals("Updated User", updatedUser.get().getName());
        assertEquals("updated@example.com", updatedUser.get().getEmail());
    }
}