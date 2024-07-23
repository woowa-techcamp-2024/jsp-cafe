package services;

import camp.woowa.jspcafe.models.User;
import camp.woowa.jspcafe.repository.InMemUserRepository;
import camp.woowa.jspcafe.repository.UserRepository;
import camp.woowa.jspcafe.services.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceTest {
    UserRepository userRepository = new InMemUserRepository();

    @Test
    void testCreateUser() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        UserService userService = new UserService(userRepository);
        // when
        String id = userService.createUser(userId, password, name, email);

        // then
        assertEquals(id, userId);
    }
}
