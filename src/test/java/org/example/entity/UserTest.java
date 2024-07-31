package org.example.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testUserCreation() {
        User user = new User("testUser", "password123", "test@example.com", "TestNickname");

        assertEquals("testUser", user.getUserId());
        assertEquals("password123", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("TestNickname", user.getNickname());
    }

    @Test
    public void testSetUserId() {
        User user = new User("testUser", "password123", "test@example.com", "TestNickname");
        user.setUserId("newUserId");

        assertEquals("newUserId", user.getUserId());
    }

    @Test
    public void testUpdate() {
        User user = new User("testUser", "password123", "test@example.com", "TestNickname");
        user.update("NewNickname", "new@example.com");

        assertEquals("NewNickname", user.getNickname());
        assertEquals("new@example.com", user.getEmail());
    }

    @Test
    public void testGetPassword() {
        User user = new User("testUser", "password123", "test@example.com", "TestNickname");

        assertEquals("password123", user.getPassword());
    }

    @Test
    public void testGetEmail() {
        User user = new User("testUser", "password123", "test@example.com", "TestNickname");

        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    public void testGetNickname() {
        User user = new User("testUser", "password123", "test@example.com", "TestNickname");

        assertEquals("TestNickname", user.getNickname());
    }
}
