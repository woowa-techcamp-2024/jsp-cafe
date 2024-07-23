package cafe.service;

import cafe.domain.db.UserDatabase;
import cafe.domain.entity.User;
import cafe.servlet.TestHttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceTest {
    private UserService userService;
    private UserDatabase userDatabase;
    private HttpServletRequest req;

    @BeforeEach
    void setUp() {
        userDatabase = new UserDatabase();
        userDatabase.save(new User("test", "test", "test", "test"));
        userService = new UserService(userDatabase);
    }

    @Test
    void find() {
        req = new TestHttpServletRequest("user/test");
        assertEquals("test", userService.find(req, null).getId());
    }

    @Test
    void findAll() {
        assertEquals(1, userService.findAll(req, null).size());
    }
}