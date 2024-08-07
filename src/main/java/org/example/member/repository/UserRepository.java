package org.example.member.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.serial.SerialException;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.member.model.dao.User;
import org.example.util.DatabaseConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private DatabaseConnectionPool connectionPool;

    @Autowired
    public UserRepository(DatabaseConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public User save(User user) throws SQLException {
        String sql = "insert into users (user_id, password, name, email) values (?, ?, ?, ?)";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getEmail());
            ps.executeUpdate();
            return user;
        } catch (SQLException e) {
            logger.error("save user error", e);
            throw e;
        }
    }

    public User update(User user) throws SQLException {
        String sql = "update users set password=?, name=?, email=? where user_id=?";
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getUserId());
            ps.executeUpdate();
            return user;
        } catch (SQLException e) {
            logger.error("update user error", e);
            throw e;
        }
    }

    public List<User> findAllUsers() throws SQLException {
        String sql = "SELECT * FROM users";

        List<User> users = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String userId = rs.getString("user_id");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String email = rs.getString("email");
                User user = User.createUser(userId, password, name, email);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new SerialException("Database Error");
        }
    }

    public User findUserByUserId(String userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String password = rs.getString("password");
                    String name = rs.getString("name");
                    String email = rs.getString("email");

                    return User.createUser(userId, password, name, email);
                }
            }
        }
        throw new SQLException("User not found");
    }

    public boolean existsByUserId(String userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        boolean exists = false;

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    exists = rs.getInt(1) > 0;
                }
            }
        }

        return exists;
    }
}
