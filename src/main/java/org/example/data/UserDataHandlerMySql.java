package org.example.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDataHandlerMySql implements UserDataHandler {
    private Logger log = LoggerFactory.getLogger(UserDataHandlerMySql.class);
    private final ConnectionProvider connectionProvider;

    public UserDataHandlerMySql(ConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public User insert(User user) {
        String sql = "INSERT INTO users (email, nickname, password, created_dt) VALUES (?, ?, ?, ?)";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getNickname());
            pstmt.setString(3, user.getPassword());
            pstmt.setTimestamp(4, Timestamp.valueOf(user.getCreatedDt()));
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    user = new User(id, user.getEmail(), user.getNickname(), user.getPassword(), user.getCreatedDt());
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

            log.debug("[UserDataHandlerMySql] inserted");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert user", e);
        }
        return user;
    }

    public User update(User user) {
        String sql = "UPDATE users SET email = ?, nickname = ?, password = ?, created_dt = ? WHERE user_id = ?";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getNickname());
            pstmt.setString(3, user.getPassword());
            pstmt.setTimestamp(4, Timestamp.valueOf(user.getCreatedDt()));
            pstmt.setLong(5, user.getUserId());
            pstmt.executeUpdate();
            log.debug("[UserDataHandlerMySql] updated");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user", e);
        }
        return user;
    }

    @Override
    public User findByUserId(Long userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getLong("user_id"),
                            rs.getString("email"),
                            rs.getString("nickname"),
                            rs.getString("password"),
                            rs.getTimestamp("created_dt").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by ID", e);
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getLong("user_id"),
                            rs.getString("email"),
                            rs.getString("nickname"),
                            rs.getString("password"),
                            rs.getTimestamp("created_dt").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by email", e);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getLong("user_id"),
                        rs.getString("email"),
                        rs.getString("nickname"),
                        rs.getString("password"),
                        rs.getTimestamp("created_dt").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all users", e);
        }
        return users;
    }
}