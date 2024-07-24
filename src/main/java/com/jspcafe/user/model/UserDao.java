package com.jspcafe.user.model;

import com.jspcafe.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {
    private final DatabaseConnector databaseConnector;

    public UserDao(final DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public void save(User user) {
        String sql = "INSERT INTO users (id, email, nickname, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.id());
            pstmt.setString(2, user.email());
            pstmt.setString(3, user.nickname());
            pstmt.setString(4, user.password());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving user", e);
        }
    }

    public Optional<User> findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getString("id"),
                            rs.getString("email"),
                            rs.getString("nickname"),
                            rs.getString("password")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by id", e);
        }
        return Optional.empty();
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getString("id"),
                            rs.getString("email"),
                            rs.getString("nickname"),
                            rs.getString("password")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by email", e);
        }
        return Optional.empty();
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("nickname"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all users", e);
        }
        return users;
    }

    public void update(final User updateUser) {
        String sql = "UPDATE users SET email = ?, nickname = ?, password = ? WHERE id = ?";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updateUser.email());
            pstmt.setString(2, updateUser.nickname());
            pstmt.setString(3, updateUser.password());
            pstmt.setString(4, updateUser.id());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user", e);
        }
    }
}
