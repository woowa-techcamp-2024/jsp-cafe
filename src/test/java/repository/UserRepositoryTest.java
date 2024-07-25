package repository;

import camp.woowa.jspcafe.models.User;
import camp.woowa.jspcafe.repository.InMemUserRepository;
import camp.woowa.jspcafe.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRepositoryTest {
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new InMemUserRepository();
        userRepository.deleteAll();
    }

    @Test
    void testSave() {
        // given
        Long id = 1L;
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";

        // when
        userRepository.save(userId, password, name, email);

        // then
        User user = userRepository.findById(id);
        assertEquals(user.getUserId(), userId);
    }

    @Test
    void testFindAll() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";

        int expected_size = 1;

        // when
        userRepository.save(userId, password, name, email);
        List<User> users = userRepository.findAll();

        // then
        assertEquals(users.size(), expected_size);
        assertEquals(users.get(expected_size - 1).getUserId(), userId);
    }

    @Test
    void testFindById() {
        // given
        Long id = 1L;
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        UserRepository userRepository = new InMemUserRepository();

        // when
        userRepository.save(userId, password, name, email);
        User user = userRepository.findById(id);

        // then
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

        // when
        userRepository.save(userId, password, name, email);
        String updatedName = "updatedName";
        String updatedEmail = "updatedEmail";
        userRepository.update(id, userId, updatedName, updatedEmail);
        User user = userRepository.findById(id);

        // then
        assertEquals(user.getName(), updatedName);
        assertEquals(user.getEmail(), updatedEmail);
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
        boolean isExisted = userRepository.isExistedByUserId(userId);

        // then
        assertTrue(isExisted);
    }
}
