package com.woowa.hyeonsik.application.dao;

import com.woowa.hyeonsik.application.domain.User;
import com.woowa.hyeonsik.server.database.DatabaseConnector;

import com.woowa.hyeonsik.server.database.JdbcException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserDao implements UserDao {
    private final DatabaseConnector databaseConnector;

    public JdbcUserDao(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public void save(User user) {
        String query = """
                INSERT INTO member(
                    member_id, password, name, email
                ) VALUES(?, ?, ?, ?)
                """;
        databaseConnector.execute(query, List.of(user.getUserId(), user.getPassword(), user.getName(), user.getEmail()));
    }

    @Override
    public void update(User user) {
        String query = """
                UPDATE member
                SET name = ?, email = ? 
                WHERE member_id = ?
                """;

        databaseConnector.execute(query, List.of(user.getName(), user.getEmail(), user.getUserId()));
    }

    @Override
    public void removeByUserId(String userId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        String query = """
                SELECT
                    member_id,
                    password,
                    name,
                    email
                FROM
                    member
                WHERE
                    member_id LIKE ?
                """;

        return databaseConnector.executeQuery(query,
                List.of(userId),
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            String memberId = resultSet.getString("member_id");
                            String password = resultSet.getString("password");
                            String name = resultSet.getString("name");
                            String email = resultSet.getString("email");

                            return Optional.of(new User(memberId, password, name, email));
                        }
                    } catch (SQLException e) {
                        throw new JdbcException(e);
                    }
                    return Optional.empty();
                }
                );
    }

    @Override
    public boolean existsByUserId(String userId) {
        Optional<User> user = findByUserId(userId);
        return user.isPresent();
    }

    @Override
    public List<User> findAll() {
        String query = """
                SELECT
                    member_id,
                    password,
                    name,
                    email
                FROM
                    member
                """;

        return databaseConnector.executeQuery(query,
                resultSet -> {
                    try {
                        List<User> users = new ArrayList<>();
                        while (resultSet.next()) {
                            String memberId = resultSet.getString("member_id");
                            String password = resultSet.getString("password");
                            String name = resultSet.getString("name");
                            String email = resultSet.getString("email");

                            users.add(new User(memberId, password, name, email));
                        }
                        return users;
                    } catch (SQLException e) {
                        throw new JdbcException(e);
                    }
                }
        );
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
