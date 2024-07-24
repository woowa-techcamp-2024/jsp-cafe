package org.example.data;

import org.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import java.sql.*;
import java.util.ArrayList;

public class UserDataHandlerMySql implements UserDataHandler {
    private Logger log = LoggerFactory.getLogger(UserDataHandlerMySql.class);

    @Override
    public User save(User user) {
        if (user.getUserId() == null) {
            return insert(user);
        } else {
            update(user);
            return user;
        }
    }

    private User insert(User user) {
        String sql = "INSERT INTO users (email, nickname, password, created_dt) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnectionManager.getConnection();
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

    private void update(User user) {
        String sql = "UPDATE users SET email = ?, nickname = ?, password = ?, created_dt = ? WHERE user_id = ?";
        try (Connection con = DatabaseConnectionManager.getConnection();
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
    }

    @Override
    public User findByUserId(Long userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection con = DatabaseConnectionManager.getConnection();
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
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection con = DatabaseConnectionManager.getConnection();
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