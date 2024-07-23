package repository;

import camp.woowa.jspcafe.models.User;
import camp.woowa.jspcafe.repository.InMemUserRepository;
import camp.woowa.jspcafe.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepositoryTest {
    @Test
    void testSave() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        UserRepository userRepository = new InMemUserRepository();
        // when
        userRepository.save(userId, password, name, email);

        // then
        User user = userRepository.findById(userId);
        assertEquals(user.getUserId(), userId);
    }

    @Test
    void testFindAll() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        UserRepository userRepository = new InMemUserRepository();

        int expected_size = 1;

        // when
        userRepository.save(userId, password, name, email);
        List<User> users = userRepository.findAll();

        // then
        assertEquals(users.size(), expected_size);
        assertEquals(users.get(expected_size - 1).getUserId(), userId);
    }
}
