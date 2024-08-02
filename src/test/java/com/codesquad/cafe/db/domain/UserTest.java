package com.codesquad.cafe.db.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("객체 생성")
    void testUserCreation() {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(1L, "username", "password", "name", "email@example.com", now, now, false);

        assertEquals(1L, user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("name", user.getName());
        assertEquals("email@example.com", user.getEmail());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
        assertFalse(user.isDeleted());
    }

    @Test
    @DisplayName("User.of 메서드")
    void testUserOfMethod() {
        User user = User.of("username", "password", "name", "email@example.com");

        assertNull(user.getId());
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("name", user.getName());
        assertEquals("email@example.com", user.getEmail());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertFalse(user.isDeleted());
    }

    @Test
    @DisplayName("생성자 유효성 검사")
    void testUserConstructorValidation() {
        LocalDateTime now = LocalDateTime.now();

        assertThrows(IllegalArgumentException.class,
                () -> new User(1L, "", "password", "name", "email@example.com", now, now, false));
        assertThrows(IllegalArgumentException.class,
                () -> new User(1L, "username", "", "name", "email@example.com", now, now, false));
        assertThrows(IllegalArgumentException.class,
                () -> new User(1L, "username", "password", "", "email@example.com", now, now, false));
        assertThrows(IllegalArgumentException.class,
                () -> new User(1L, "username", "password", "name", "", now, now, false));
        assertThrows(IllegalArgumentException.class,
                () -> new User(1L, "username", "password", "name", "email@example.com", null, now, false));
        assertThrows(IllegalArgumentException.class,
                () -> new User(1L, "username", "password", "name", "email@example.com", now, now.minusDays(1), false));
    }

    @Test
    @DisplayName("업데이트 메서드")
    void testUpdate() {
        User user = User.of("username", "password", "name", "email@example.com");
        user.update("newPassword", "newName", "newEmail@example.com");

        assertEquals("newPassword", user.getPassword());
        assertEquals("newName", user.getName());
        assertEquals("newEmail@example.com", user.getEmail());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    @DisplayName("삭제 메서드")
    void testDelete() {
        User user = User.of("username", "password", "name", "email@example.com");
        user.delete();

        assertTrue(user.isDeleted());
        assertTrue(user.getUpdatedAt().isAfter(user.getCreatedAt()));
    }

    @Test
    @DisplayName("equals - id 없는 객체")
    void testEqualsDifferentCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        User firstUser = new User(null, "username", "password", "name", "email@example.com", now, now, false);
        User secondUser = new User(null, "username", "password", "name", "email@example.com", now, now, false);

        assertEquals(firstUser, secondUser);
    }

    @Test
    @DisplayName("equals - id 있는 객체")
    void testEqualsWitId() {
        LocalDateTime now = LocalDateTime.now();
        User firstUser = new User(100L, "username", "password", "name", "email@example.com", now, now, false);
        User secondUser = new User(100L, "username", "password", "name", "email@example.com", now, now, false);

        assertEquals(firstUser, secondUser);
    }

}
