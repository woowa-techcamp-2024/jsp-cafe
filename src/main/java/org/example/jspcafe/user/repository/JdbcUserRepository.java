package org.example.jspcafe.user.repository;

import org.example.jspcafe.Component;
import org.example.jspcafe.DatabaseConnectionManager;
import org.example.jspcafe.ReflectionIdFieldExtractor;
import org.example.jspcafe.user.model.User;

import java.sql.*;
import java.util.*;

@Component
public class JdbcUserRepository extends ReflectionIdFieldExtractor<User> implements UserRepository {

    private final DatabaseConnectionManager connectionManger;

    public JdbcUserRepository(DatabaseConnectionManager connectionManger) {
        super(User.class);
        this.connectionManger = connectionManger;
    }

    @Override
    public User save(User user) {
        if(user == null) {
            throw new IllegalArgumentException("Entity는 null일 수 없습니다.");
        }

        if(user.getUserId() != null) {
            update(user);
            return user;
        }
        String sql = "INSERT INTO users (nickname, email, password, created_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = connectionManger.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getNickname().getValue());
            pstmt.setString(2, user.getEmail().getValue());
            pstmt.setString(3, user.getPassword().getValue());
            pstmt.setTimestamp(4, Timestamp.valueOf(user.getCreatedAt()));
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return putId(generatedKeys.getLong(1), user);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("user 저장 중 오류 발생 "+e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = connectionManger.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUser(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("User를 찾는 중 오류 발생", e);
        }

        return Optional.empty();
    }

    @Override
    public void delete(User user) {
        if(user.getUserId() == null) {
            throw new IllegalArgumentException("Id는 null일 수 없습니다.");
        }
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = connectionManger.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, user.getUserId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET nickname = ?, email = ?, password = ? WHERE user_id = ?";

        try (Connection conn = connectionManger.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getNickname().getValue());
            pstmt.setString(2, user.getEmail().getValue());
            pstmt.setString(3, user.getPassword().getValue());
            if(user.getUserId() == null) {
                throw new IllegalArgumentException("Id는 null일 수 없습니다.");
            }
            pstmt.setLong(4, user.getUserId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating user", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = connectionManger.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUser(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by email", e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        String sql = "SELECT * FROM users WHERE nickname = ?";

        try (Connection conn = connectionManger.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nickname);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUser(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by nickname", e);
        }

        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";

        try (Connection conn = connectionManger.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all users", e);
        }
    }

    @Override
    public List<User> findAllById(Collection<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList(); // 비어 있는 경우 빈 리스트 반환
        }
        String placeholders = String.join(",", "?".repeat(userIds.size()).split(""));
        String sql = "SELECT * FROM users WHERE user_id IN (" + placeholders + ")";

        try (Connection conn = connectionManger.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int index = 1;
            for (Long id : userIds) {
                pstmt.setLong(index++, id);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                List<User> users = new ArrayList<>();
                while (rs.next()) {
                    users.add(mapRowToUser(rs));
                }
                return users;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding users by IDs", e);
        }
    }


    @Override
    public void deleteAllInBatch() {
        String sql = "DELETE FROM users";

        try (Connection conn = connectionManger.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all users", e);
        }
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getString("nickname"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
        return putId(rs.getLong("user_id"), user);
    }
}
