package camp.woowa.jspcafe.repository;

import camp.woowa.jspcafe.db.DatabaseManager;
import camp.woowa.jspcafe.db.MySQLDatabaseManager;
import camp.woowa.jspcafe.model.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MySQLUserRepositoryTest {
    static UserRepository userRepository;
    static DatabaseManager dm;

    @BeforeAll
    static void setUpAll() {
        dm = new MySQLDatabaseManager();
        userRepository = new MySQLUserRepository(dm);
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterAll
    static void tearDownAll() {
        userRepository.deleteAll();
    }

    @Test
    void testSaveAndFindById() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";
        User save = new User(userId, password, name, email);

        // when
        Long id = userRepository.save(save);

        // then
        assertEquals(userId, userRepository.findById(id).getUserId());
    }

    @Test
    void testFindAll() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";

        int expected_size = 1;

        User save = new User(userId, password, name, email);

        // when
        Long id = userRepository.save(save);

        List<User> users = userRepository.findAll();

        // then
        assertEquals(users.size(), expected_size);
        assertEquals(users.get(expected_size - 1).getUserId(), userId);
    }

    @Test
    void testUpdate() {
        // given
        String userId = "userId";
        String password = "password";
        String name = "name";
        String email = "email";

        User save = new User(userId, password, name, email);

        // when
        Long id = userRepository.save(save);
        String updatedName = "updatedName";
        String updatedEmail = "updatedEmail";
        User target = userRepository.findById(id);
        target.update(updatedName, updatedEmail);
        userRepository.update(target);
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
        User save = new User(userId, password, name, email);

        userRepository.save(save);

        // when
        boolean isExisted = userRepository.isExistedByUserId(userId);

        // then
        assertTrue(isExisted);
    }
}
