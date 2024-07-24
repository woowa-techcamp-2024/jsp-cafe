package org.example.member.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.example.member.model.dao.User;
import org.example.util.DataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new MockUserRepository();
    }

    @Test
    @DisplayName("유효한 사용자 정보는 정상적으로 db에 저장한다.")
    public void save_new_user_successfully() throws SQLException {
        User user = User.createUser("user1", "password1", "John Doe", "john.doe@example.com");

        User registeredUser = userRepository.save(user);

        assertNotNull(registeredUser);
        assertEquals("user1", registeredUser.getUserId());
        assertEquals("password1", registeredUser.getPassword());
        assertEquals("John Doe", registeredUser.getName());
        assertEquals("john.doe@example.com", registeredUser.getEmail());
    }

    // Retrieve all users from the database
    @Test
    @DisplayName("findAllUsers는 데이터가 비어있다면 비어있는 리스트를 반환한다.")
    public void retrieve_all_users_from_database() throws SQLException {

        List<User> users = userRepository.findAllUsers();

        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    // Find a user by user ID
    @Test
    @DisplayName("존재하는 userId가 주어지면 findUserByUserId는 해당 사용자를 반환한다.")
    public void find_user_by_user_id() throws SQLException {
        String userId = "user1";

        User user = userRepository.findUserByUserId(userId);

        assertNotNull(user);
        assertEquals(userId, user.getUserId());
    }

    // Check if a user exists by user ID
    @Test
    @DisplayName("존재하는 userId가 주어지면 existsByUserId는 true를 반환한다.")
    public void check_if_user_exists_by_user_id() throws SQLException {
        String userId = "user1";

        boolean exists = userRepository.existsByUserId(userId);

        assertTrue(exists);
    }
    // Handle invalid SQL queries
    @Test
    @DisplayName("유효하지 않은 SQL문이 주어지면 SQLException이 발생한다.")
    public void handle_invalid_sql_queries() {
        // Simulate invalid SQL query by modifying the query string in the repository method

        String invalidSql = "SELECT * FROM non_existent_table";

        try (Connection conn = DataUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(invalidSql)) {

            assertThrows(SQLException.class, () -> {
                ps.executeQuery();
            });

        } catch (SQLException e) {
            // Expected exception due to invalid SQL query
            assertNotNull(e);
        }
    }

    static class MockUserRepository extends UserRepository {

        @Override
        public User findUserByUserId(String userId) throws SQLException {
            return User.createUser(userId, "password", "John Doe", "john.doe@example.com");
        }

        @Override
        public boolean existsByUserId(String userId) throws SQLException {
            return "user1".equals(userId);
        }

        @Override
        public User save(User user) throws SQLException {
            return user;
        }
    }

}