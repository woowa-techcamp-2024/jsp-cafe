package services;

import camp.woowa.jspcafe.models.User;
import camp.woowa.jspcafe.repository.InMemUserRepository;
import camp.woowa.jspcafe.repository.UserRepository;
import camp.woowa.jspcafe.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserRepository userRepository;
    UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = new InMemUserRepository();
        userService = new UserService(userRepository);

        userRepository.deleteAll();
    }

    @Test
    void testCreateUser() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";

        // when
        Long id = userService.createUser(userId, password, name, email);

        User user = userService.findById(id);
        // then
        assertEquals(userId, user.getUserId());
    }

    @Test
    void testFindAll() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";

        userService.createUser(userId, password, name, email);

        int expected_size = 1;

        // when
        List<User> users = userService.findAll();

        // then
        assertEquals(expected_size, users.size());
    }

    @Test
    void testFindById() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        Long id = userRepository.save(userId, password, name, email);


        // when
        User user = userService.findById(id);

        // then
        assertEquals(user.getUserId(), userId);
    }

    @Test
    void testUpdate() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        Long id = userRepository.save(userId, password, name, email);
        String updatedUserId = "updatedUserId";
        String updatedName = "updatedName";
        String updatedEmail = "updatedEmail";
        User old_user = userService.findById(id);

        // when
        Long updated_id = userService.update(old_user, id, password, updatedName, updatedEmail);

        // then
        User user = userService.findById(id);
        User updated = userRepository.findById(updated_id);

        assertEquals(user, updated);
        assertEquals(user.getUserId(), updatedUserId);
        assertEquals(user.getName(), updatedName);
        assertEquals(user.getEmail(), updatedEmail);
    }

    @Test
    void testFailUpdateByIncorrectPassword() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        Long id = userRepository.save(userId, password, name, email);
        String updatedName = "updatedName";
        String updatedEmail = "updatedEmail";

        User old_user = userRepository.findById(id);
        // when
        // then
        assertThrows(RuntimeException.class, () -> userService.update(old_user, id, "1234", updatedName, updatedEmail));
    }

    @Test
    void testIsExistedByUserId() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        userRepository.save(userId, password, name, email);

        // when
        boolean isExisted = userService.isExistedByUserId(userId);

        // then
        assertTrue(isExisted);
    }

    @Test
    void testLogin() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        Long id = userRepository.save(userId, password, name, email);

        // when
        User user = userService.login(userId, password);

        // then
        assertEquals(user.getUserId(), userId);
    }
}
