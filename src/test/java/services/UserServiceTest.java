package services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserServiceTest {

    @Test
    void testCreateUser() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        UserService userService = new UserService();
        // when
        User user = userService.createUser(userId, password, name, email);

        // then
        assertEquals(user.getUserId(), userId);
    }
}
