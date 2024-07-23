package models;

import camp.woowa.jspcafe.models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

}
