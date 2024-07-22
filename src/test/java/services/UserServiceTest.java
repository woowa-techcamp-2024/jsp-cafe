package services;

import org.junit.jupiter.api.Test;

public class UserServiceTest {

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
        assertThat(user.getUserId()).isEqualTo(userId);
    }
}
