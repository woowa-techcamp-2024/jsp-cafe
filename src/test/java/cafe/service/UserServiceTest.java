package cafe.service;

import cafe.domain.db.UserDatabase;
import cafe.domain.entity.User;
import cafe.servlet.TestHttpServletRequest;
import cafe.servlet.TestHttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private UserDatabase userDatabase;
    private TestHttpServletRequest req;
    private TestHttpServletResponse resp;

    @BeforeEach
    public void setUp() {
        userDatabase = new UserDatabase();
        userService = new UserService(userDatabase);
        req = new TestHttpServletRequest();
        resp = new TestHttpServletResponse();
    }

    @Test
    @DisplayName("User 객체를 데이터베이스에 저장하는 기능 테스트")
    public void testSave() {
        req.setParameter("userId", "user1");
        req.setParameter("password", "password123");
        req.setParameter("name", "John Doe");
        req.setParameter("email", "john.doe@example.com");

        userService.save(req, resp);

        Map<String, User> allUsers = userDatabase.findAll();
        assertEquals(1, allUsers.size());
        User savedUser = allUsers.values().iterator().next();
        assertEquals("user1", savedUser.getId());
        assertEquals("password123", savedUser.getPassword());
        assertEquals("John Doe", savedUser.getName());
        assertEquals("john.doe@example.com", savedUser.getEmail());
    }

    @Test
    @DisplayName("ID로 User 객체를 조회하는 기능 테스트")
    public void testFind() {
        User user = User.of("user1", "password123", "John Doe", "john.doe@example.com");
        userDatabase.save(user);

        String userId = userDatabase.findAll().keySet().iterator().next();
        req.setRequestURI("/users/" + userId);

        User foundUser = userService.find(req, resp);

        assertNotNull(foundUser);
        assertEquals("user1", foundUser.getId());
        assertEquals("password123", foundUser.getPassword());
        assertEquals("John Doe", foundUser.getName());
        assertEquals("john.doe@example.com", foundUser.getEmail());
    }

    @Test
    @DisplayName("모든 User 객체를 조회하는 기능 테스트")
    public void testFindAll() {
        User user1 = User.of("user1", "password123", "John Doe", "john.doe@example.com");
        User user2 = User.of("user2", "password456", "Jane Doe", "jane.doe@example.com");
        userDatabase.save(user1);
        userDatabase.save(user2);

        Map<String, User> allUsers = userService.findAll(req, resp);

        assertEquals(2, allUsers.size());
        assertTrue(allUsers.containsValue(user1));
        assertTrue(allUsers.containsValue(user2));
    }

    @Test
    @DisplayName("User 객체를 업데이트하는 기능 테스트")
    public void testUpdate() {
        User user = User.of("user1", "password123", "John Doe", "john.doe@example.com");
        userDatabase.save(user);

        String userId = userDatabase.findAll().keySet().iterator().next();
        req.setRequestURI("/users/" + userId);
        req.setParameter("before-password", "password123");
        req.setParameter("password", "newpassword123");
        req.setParameter("name", "John Doe Updated");
        req.setParameter("email", "john.doe.updated@example.com");

        userService.update(req, resp);

        User updatedUser = userDatabase.find(userId);
        assertNotNull(updatedUser);
        assertEquals("newpassword123", updatedUser.getPassword());
        assertEquals("John Doe Updated", updatedUser.getName());
        assertEquals("john.doe.updated@example.com", updatedUser.getEmail());
    }
}