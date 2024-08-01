package codesquad.global.dao;

import codesquad.common.db.connection.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlUserQuery implements UserQuery {
    private ConnectionManager connectionManager;

    public MySqlUserQuery(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Optional<UserResponse> findById(Long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = "select user_id, name, email from users where id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String userId = resultSet.getString("user_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                return Optional.of(new UserResponse(id, userId, name, email));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public List<UserResponse> findAll(QueryRequest queryRequest) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = "select user_id, name, email from users";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            List<UserResponse> userResponses = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String userId = resultSet.getString("user_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                userResponses.add(new UserResponse(id, userId, name, email));
            }
            return userResponses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }
}
