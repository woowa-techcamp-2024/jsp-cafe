package cafe.domain.db;

import cafe.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDatabaseTest {
    private UserDatabase userDatabase;

    @BeforeEach
    void setUp() {
        userDatabase = new UserDatabase();
    }

    @Test
    void save() {
        userDatabase.save(new User("test", "test", "test", "test"));
        assertEquals(1, userDatabase.findAll().size());
    }

    @Test
    void find() {
        userDatabase.save(new User("test", "test", "test", "test"));
        User user = userDatabase.find("test");
        assertEquals("test", user.getId());
    }

    @Test
    void findAll() {
        userDatabase.save(new User("test", "test", "test", "test"));
        userDatabase.save(new User("test2", "test2", "test2", "test2"));
        assertEquals(2, userDatabase.findAll().size());
    }
}