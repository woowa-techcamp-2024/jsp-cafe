package codesquad.infra;

import codesquad.domain.user.User;
import codesquad.domain.user.UserDao;
import codesquad.exception.DuplicateIdException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlUserDao implements UserDao {
    private ConnectionManager connectionManager;

    public MySqlUserDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Long save(User user) throws DuplicateIdException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = "insert into users(user_id,password,name,email) values(?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                long id = resultSet.getLong(1);
                user = new User(id, user);
                return id;
            }
            throw new SQLException("Failed to insert article");
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new DuplicateIdException("중복된 아이디 입니다.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = "select * from users where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String userId = resultSet.getString("user_id");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                return Optional.of(new User(id, userId, password, name, email));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = "select * from users where user_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                return Optional.of(new User(id, userId, password, name, email));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public List<User> findAll() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = "select * from users";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String userId = resultSet.getString("user_id");
                String password = resultSet.getString("password");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                users.add(new User(id, userId, password, name, email));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public void update(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionManager.getConnection();
            String sql = "update users set name = ?, email = ? where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setLong(3, user.getId());
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0) {
                throw new SQLException("Failed to update user");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement);
        }
    }
}
