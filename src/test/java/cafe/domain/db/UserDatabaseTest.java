package cafe.domain.db;

import cafe.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserDatabaseTest {
    private UserDatabase userDatabase;
    private User user;

    @BeforeEach
    public void setUp() {
        userDatabase = new UserDatabase();
        user = User.of("user1", "password123", "John Doe", "john.doe@example.com");
    }

    @Test
    @DisplayName("User를 데이터베이스에 저장하고 조회하는 기능 테스트")
    public void testSaveAndFind() {
        userDatabase.save(user);
        Map<String, User> allUsers = userDatabase.findAll();
        User foundUser = allUsers.values().iterator().next();

        assertEquals(1, allUsers.size());
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getPassword(), foundUser.getPassword());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    @DisplayName("데이터베이스에서 User를 ID로 조회하는 기능 테스트")
    public void testFindById() {
        userDatabase.save(user);
        String userId = userDatabase.findAll().keySet().iterator().next();
        User foundUser = userDatabase.find(userId);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getPassword(), foundUser.getPassword());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 User를 조회할 때 null을 반환하는지 테스트")
    public void testFindByIdNotFound() {
        User foundUser = userDatabase.find(UUID.randomUUID().toString());
        assertNull(foundUser);
    }

    @Test
    @DisplayName("모든 User를 조회하는 기능 테스트")
    public void testFindAll() {
        userDatabase.save(user);
        Map<String, User> allUsers = userDatabase.findAll();

        assertNotNull(allUsers);
        assertEquals(1, allUsers.size());
        User foundUser = allUsers.values().iterator().next();
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    @DisplayName("User 정보를 업데이트하는 기능 테스트")
    public void testUpdate() {
        userDatabase.save(user);
        String userId = userDatabase.findAll().keySet().iterator().next();
        User updatedUser = User.of("user1", "newpassword123", "John Doe", "john.doe@example.com");

        userDatabase.update(userId, updatedUser);
        User foundUser = userDatabase.find(userId);

        assertNotNull(foundUser);
        assertEquals(updatedUser.getPassword(), foundUser.getPassword());
        assertEquals(updatedUser.getName(), foundUser.getName());
        assertEquals(updatedUser.getEmail(), foundUser.getEmail());
    }
}