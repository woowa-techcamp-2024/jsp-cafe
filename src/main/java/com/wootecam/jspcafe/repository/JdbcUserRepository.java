package com.wootecam.jspcafe.repository;

import com.wootecam.jspcafe.config.JdbcTemplate;
import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.domain.UserRepository;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        this.jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS users\n"
                        + "(\n"
                        + "    id       BIGINT AUTO_INCREMENT PRIMARY KEY,\n"
                        + "    user_id  VARCHAR(30),\n"
                        + "    password VARCHAR(30),\n"
                        + "    name     VARCHAR(30),\n"
                        + "    email    VARCHAR(30)\n"
                        + ")"
        );
    }

    @Override
    public void save(final User user) {
        String query = "INSERT INTO users(user_id, password, name, email) VALUES(?, ?, ?, ?)";

        jdbcTemplate.update(
                query,
                ps -> {
                    ps.setString(1, user.getUserId());
                    ps.setString(2, user.getPassword());
                    ps.setString(3, user.getName());
                    ps.setString(4, user.getEmail());
                }
        );
    }

    @Override
    public List<User> findAll() {
        String query = "SELECT id, user_id, password, name, email FROM users";

        return jdbcTemplate.selectAll(
                query,
                resultSet -> new User(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)
                )
        );
    }

    @Override
    public Optional<User> findById(final Long id) {
        String query = "SELECT id, user_id, password, name, email FROM users WHERE id = ?";

        User user = jdbcTemplate.selectOne(
                query,
                ps -> ps.setLong(1, id),
                resultSet -> new User(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)
                )
        );

        return Optional.ofNullable(user);
    }

    @Override
    public void update(final User user) {
        String query = "UPDATE users SET password = ?, name = ?, email = ? WHERE id = ?";

        jdbcTemplate.update(
                query,
                ps -> {
                    ps.setString(1, user.getPassword());
                    ps.setString(2, user.getName());
                    ps.setString(3, user.getEmail());
                    ps.setLong(4, user.getId());
                }
        );
    }

    @Override
    public Optional<User> findByUserId(final String userId) {
        String query = "SELECT id, user_id, password, name, email FROM users WHERE user_id = ?";

        User user = jdbcTemplate.selectOne(
                query,
                ps -> ps.setString(1, userId),
                resultSet -> new User(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)
                )
        );

        return Optional.ofNullable(user);
    }
}
