package org.example.cafe.infrastructure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.cafe.common.error.CafeException;
import org.example.cafe.domain.User;
import org.example.cafe.domain.UserRepository;
import org.example.cafe.infrastructure.database.DbConnector;

public class UserJdbcRepository implements UserRepository {

    private static final String INSERT = "INSERT INTO `USER` (user_id, password, nickname, email) VALUES (?, ?, ?, ?)";
    private static final String SELECT = "SELECT * FROM `USER`";
    private static final String SELECT_BY_ID = "SELECT * FROM `USER` WHERE user_id = ?";
    private static final String DELETE = "DELETE FROM `USER`";
    private static final String UPDATE = "UPDATE `USER` SET password=?, nickname=?, email=? where user_id=?";

    private final DbConnector dbConnector;

    public UserJdbcRepository(DbConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public void save(User user) {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT)) {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getNickname());
            preparedStatement.setString(4, user.getEmail());

            int row = preparedStatement.executeUpdate();
            if (row != 1) {
                throw new CafeException("Failed to insert user " + user.getUserId());
            }
        } catch (SQLException e) {
            throw new CafeException("Failed to insert user " + user.getUserId(), e);
        }
    }

    public void update(User user) {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setString(2, user.getNickname());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getUserId());

            int row = preparedStatement.executeUpdate();
            if (row != 1) {
                throw new CafeException("Failed to update user " + user.getUserId());
            }
        } catch (SQLException e) {
            throw new CafeException("Failed to update user " + user.getUserId(), e);
        }
    }

    public User findById(String id) {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ID)) {
            preparedStatement.setString(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(resultSet.getString("user_id"),
                            resultSet.getString("password"),
                            resultSet.getString("nickname"),
                            resultSet.getString("email"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new CafeException("Failed to find user " + id, e);
        }
    }

    public List<User> findAll() {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<User> result = new ArrayList<>();

                while (resultSet.next()) {
                    result.add(new User(resultSet.getString("user_id"),
                            resultSet.getString("password"),
                            resultSet.getString("nickname"),
                            resultSet.getString("email")));
                }

                return result;
            }
        } catch (SQLException e) {
            throw new CafeException("Failed to find users", e);
        }
    }

    public void deleteAll() {
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new CafeException("Failed to delete users", e);
        }
    }
}
