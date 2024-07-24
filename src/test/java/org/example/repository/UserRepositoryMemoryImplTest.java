package org.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserRepositoryMemoryImplTest {

    private UserRepositoryMemoryImpl userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryMemoryImpl();
        userRepository.clear();
        // Initialize with a user if necessary
    }

    @Test
    void testAddUser() {
        User newUser = new User("123","123", "testUser@naver.com", "testUser");
        userRepository.saveUser(newUser);
        Optional<User> retrievedUser = userRepository.getUserByUserId("123");
        assertTrue(retrievedUser.isPresent());
        assertEquals("testUser", retrievedUser.get().getNickname());
    }

    @Test
    void testFindUser() {
        User newUser = new User("123","2", "findUser", "findUser");
        userRepository.saveUser(newUser);
        Optional<User> foundUser = userRepository.getUserByUserId("123");
        assertTrue(foundUser.isPresent());
        assertEquals("findUser", foundUser.get().getNickname());
    }

    @Test
    void testRemoveUser() {
        User newUser = new User("123", "3", "removeUser", "removeUser");
        userRepository.saveUser(newUser);
        userRepository.deleteUser("123");
        Optional<User> userAfterRemoval = userRepository.getUserByUserId("123");
        assertFalse(userAfterRemoval.isPresent());
    }
}