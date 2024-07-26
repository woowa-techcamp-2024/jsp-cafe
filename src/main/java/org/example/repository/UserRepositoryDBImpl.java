package org.example.repository;

import java.util.ArrayList;
import java.util.List;
import org.example.entity.User;
import org.example.DatabaseManager;
import java.sql.*;
import java.util.Optional;

public class UserRepositoryDBImpl implements UserRepository {
    private static UserRepository instance;

    private UserRepositoryDBImpl() {
        // Private constructor to prevent instantiation
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepositoryDBImpl();
            instance.save(new User("test", "test", "test@naver.com", "test"));
            instance.save(new User("test2", "test2", "test2@naver.com", "test2"));
        }
        return instance;
    }

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (user_id, nickname, email, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getNickname());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Optional<User> getUserByUserId(String userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        User user = null;
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                        rs.getString("user_id"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(user);
    }

    @Override
    public void clear() {
        String sql = "DELETE FROM users";
        try (Connection conn = DatabaseManager.getConnection();
            Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseManager.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User(
                    rs.getString("user_id"),
                    rs.getString("nickname"),
                    rs.getString("email"),
                    rs.getString("password")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void updateUser(String userId, String nickname, String email) {
        String sql = "UPDATE users SET nickname = ?, email = ? WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nickname);
            pstmt.setString(2, email);
            pstmt.setString(3, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}