package repository.users;

import domain.User;
import utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JDBCUserRepository implements UserRepository {

    @Override
    public void saveUser(User user)  {
        String sql = "INSERT INTO Users (userId, password, name, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getLong(1));
                }
            }
            DatabaseUtils.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM Users WHERE id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getLong("id"),
                            rs.getString("userId"),
                            rs.getString("password"),
                            rs.getString("name"),
                            rs.getString("email")
                    ));
                }
            }
            DatabaseUtils.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUserId(String password) {
        String sql = "SELECT * FROM Users WHERE userId = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getLong("id"),
                            rs.getString("userId"),
                            rs.getString("password"),
                            rs.getString("name"),
                            rs.getString("email")
                    ));
                }
            }
            DatabaseUtils.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getLong("id"),
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }
            DatabaseUtils.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE Users SET userId = ?, password = ?, name = ?, email = ? WHERE id = ?";
        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
            pstmt.setLong(5, user.getId());
            pstmt.executeUpdate();
            DatabaseUtils.closeConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
