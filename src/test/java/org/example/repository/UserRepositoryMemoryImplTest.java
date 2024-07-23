package org.example.repository;

import org.example.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
        User newUser = new User("123", "testUser@naver.com", "testUser");
        userRepository.saveUser(newUser);
        Optional<User> retrievedUser = userRepository.getUser(1);
        assertTrue(retrievedUser.isPresent());
        assertEquals("testUser", retrievedUser.get().getNickname());
    }

    @Test
    void testFindUser() {
        User newUser = new User("2", "findUser", "findUser");
        userRepository.saveUser(newUser);
        Optional<User> foundUser = userRepository.getUser(1);
        assertTrue(foundUser.isPresent());
        assertEquals("findUser", foundUser.get().getNickname());
    }

    @Test
    void testRemoveUser() {
        User newUser = new User("3", "removeUser", "removeUser");
        userRepository.saveUser(newUser);
        userRepository.deleteUser(1);
        Optional<User> userAfterRemoval = userRepository.getUser(1);
        assertFalse(userAfterRemoval.isPresent());
    }
}