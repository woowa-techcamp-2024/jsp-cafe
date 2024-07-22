package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UserTest {
    @Test
    void testCreateUser() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        // when
        User user = new User(userId, password, name, email);

        // then
        assertEquals(user.getUserId(), userId);
    }
}
