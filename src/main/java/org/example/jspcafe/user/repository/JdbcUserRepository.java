package org.example.jspcafe.user.repository;

import org.example.jspcafe.database.SimpleConnectionPool;
import org.example.jspcafe.user.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    @Override
    public Long save(User user) {
        String sql = "INSERT INTO Users (user_id, password, nickname, email) VALUES (?, ?, ?, ?)";
9        Connection conn = SimpleConnectionPool.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getNickname());
            pstmt.setString(4, user.getEmail());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally {
            SimpleConnectionPool.releaseConnection(conn);
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE Users SET password = ?, nickname = ?, email = ? WHERE id = ?";
        Connection conn = SimpleConnectionPool.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getNickname());
            pstmt.setString(3, user.getEmail());
            pstmt.setLong(4, user.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SimpleConnectionPool.releaseConnection(conn);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        Connection conn = SimpleConnectionPool.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = User.builder()
                        .id(rs.getLong("id"))
                        .userId(rs.getString("user_id"))
                        .email(rs.getString("email"))
                        .nickname(rs.getString("nickname"))
                        .password(rs.getString("password"))
                        .build();
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SimpleConnectionPool.releaseConnection(conn);
        }
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM Users WHERE id = ?";

        Connection conn = SimpleConnectionPool.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = User.builder()
                            .id(rs.getLong("id"))
                            .userId(rs.getString("user_id"))
                            .email(rs.getString("email"))
                            .nickname(rs.getString("nickname"))
                            .password(rs.getString("password"))
                            .build();
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SimpleConnectionPool.releaseConnection(conn);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        String sql = "SELECT * FROM Users WHERE user_id = ?";
        Connection conn = SimpleConnectionPool.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = User.builder()
                            .id(rs.getLong("id"))
                            .userId(rs.getString("user_id"))
                            .email(rs.getString("email"))
                            .nickname(rs.getString("nickname"))
                            .password(rs.getString("password"))
                            .build();
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SimpleConnectionPool.releaseConnection(conn);
        }
        return Optional.empty();
    }
}