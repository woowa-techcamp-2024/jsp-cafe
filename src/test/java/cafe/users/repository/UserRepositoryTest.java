package cafe.users.repository;

import cafe.users.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class UserRepositoryTest {
    private UserRepository userRepository;

    protected abstract UserRepository createUserRepository();

    @BeforeAll
    void setUp() {
        userRepository = createUserRepository();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    public void insertUser() {
        User user = new User("user1", "user1@example.com", "User One", "password");
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getId());
    }

    @Test
    void updateUser() {
        User user = new User("user1", "user1@example.com", "UserOne", "password");
        User savedUser = userRepository.save(user);
        String updatedUsername = "UserOneUpdated";
        String updatedEmail = "update@exmaple.com";
        String updatedPassword = "updatedPassword";
        User updatedUser = savedUser.withUsername(updatedUsername).withEmail(updatedEmail).withPassword(updatedPassword);

        savedUser = userRepository.save(updatedUser);

        assertEquals(savedUser.getId(), updatedUser.getId());
        assertEquals(updatedUsername, savedUser.getUsername());
        assertEquals(updatedEmail, savedUser.getEmail());
        assertEquals(updatedPassword, savedUser.getPassword());
    }

    @Test
    public void testFindAllUsers() {
        User user1 = new User("user1", "user1@example.com", "User One", "password");
        User user2 = new User("user2", "user2@example.com", "User Two", "password");
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
    }

    @Test
    public void testFindUserById() {
        User user = new User("user1", "user1@example.com", "User One", "password");
        User savedUser = userRepository.save(user);

        User foundUser = userRepository.findById(savedUser.getId());
        assertEquals("User One", foundUser.getUsername());
    }

    @Test
    public void testDeleteAllUsers() {
        User user1 = new User("user1", "user1@example.com", "User One", "password");
        User user2 = new User("user2", "user2@example.com", "User Two", "password");
        userRepository.save(user1);
        userRepository.save(user2);

        userRepository.deleteAll();
        assertTrue(userRepository.findAll().isEmpty());
    }
}