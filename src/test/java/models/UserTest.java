package models;

import camp.woowa.jspcafe.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class UserTest {
    @Test
    void testUser() {
        // given
        Long id = 1L;
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        // when
        User user = new User(id, userId, password, name, email);

        // then
        assertEquals(user.getId(), id);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getName(), name);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getUserId(), userId);
    }

    @Test
    void testUpdate() {
        // given
        Long id = 1L;
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        User user = new User(id, userId, password, name, email);
        String updatedUserId = "updatedUserId";
        String updatedName = "updatedName";
        String updatedEmail = "updatedEmail";
        // when
        user.update(updatedName, updatedEmail);

        // then
        assertEquals(user.getName(), updatedName);
        assertEquals(user.getEmail(), updatedEmail);
    }

    @Test
    void testPasswordValidate() {
        // given
        Long id = 1L;
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        User user = new User(id, userId, password, name, email);
        // when
        boolean result = user.validatePassword(password);
        // then
        assertTrue(result);
    }

}
