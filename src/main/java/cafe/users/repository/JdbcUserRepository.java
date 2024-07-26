package cafe.users.repository;

import cafe.database.ConnectionPool;
import cafe.users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserRepository implements UserRepository {
    private final ConnectionPool connectionPool;

    public JdbcUserRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            return insert(user);
        } else {
            return update(user);
        }
    }

    private User update(User user) {
        String sql = "UPDATE users SET user_id = ?, email = ?, username = ?, password = ? WHERE user_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            // use the connection to update the user
            statement.setString(1, user.getUserId());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getUserId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Failed to update user, no rows affected.");
            }
            return user;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private User insert(User user) {
        String sql = "INSERT INTO users (user_id, email, username, password) VALUES (?, ?, ?, ?)";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // use the connection to save the user
            statement.setString(1, user.getUserId());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getPassword());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Failed to save user, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return user.withId(generatedKeys.getLong(1));
                } else {
                    throw new RuntimeException("Failed to save user, no ID obtained.");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getLong("id"),
                        resultSet.getString("user_id"),
                        resultSet.getString("email"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                ));
            }
            return users;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getLong("id"),
                            resultSet.getString("user_id"),
                            resultSet.getString("email"),
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    );
                }
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findByUserId(String userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getLong("id"),
                            resultSet.getString("user_id"),
                            resultSet.getString("email"),
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    );
                }
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM users");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
