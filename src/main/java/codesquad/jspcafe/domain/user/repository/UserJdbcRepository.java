package codesquad.jspcafe.domain.user.repository;

import codesquad.jspcafe.common.database.MySQLConnectionManager;
import codesquad.jspcafe.domain.user.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserJdbcRepository implements UserRepository {

    private static final Logger log = LoggerFactory.getLogger(UserJdbcRepository.class);

    private final MySQLConnectionManager mySQLConnectionManager;

    public UserJdbcRepository(MySQLConnectionManager mySQLConnectionManager) {
        this.mySQLConnectionManager = mySQLConnectionManager;
    }

    @Override
    public User save(User user) {
        String insertQuery = "INSERT INTO users (user_id, password, username, email) VALUES (?, ?, ?, ?)";
        try (Connection connection = mySQLConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getEmail());
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                Long generatedId = generatedKey.getLong(1);
                user.setId(generatedId);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다.");
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return user;
    }

    @Override
    public User update(User user) {
        String updateQuery = "UPDATE users SET username = ?, email = ? WHERE id = ?";
        try (Connection connection = mySQLConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setLong(3, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return user;
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        String findByUserIdQuery = "SELECT id, user_id, password, username, email FROM users WHERE user_id = ?";
        try (Connection connection = mySQLConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(findByUserIdQuery)) {
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User(
                    resultSet.getLong("id"),
                    resultSet.getString("user_id"),
                    resultSet.getString("password"),
                    resultSet.getString("username"),
                    resultSet.getString("email")
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String findAllQuery = "SELECT id, user_id, password, username, email FROM users";
        try (Connection connection = mySQLConnectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(findAllQuery)) {
            while (resultSet.next()) {
                User user = new User(
                    resultSet.getLong("id"),
                    resultSet.getString("user_id"),
                    resultSet.getString("password"),
                    resultSet.getString("username"),
                    resultSet.getString("email")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Collections.unmodifiableList(users);
    }
}
