package codesqaud.app.dao;

import codesqaud.app.model.User;

import java.util.List;
import java.util.Optional;

public class DbUserDao implements UserDao {
    private static final RowMapper<User> USER_ROW_MAPPER = (resultSet) -> new User(
            resultSet.getLong("id"),
            resultSet.getString("user_id"),
            resultSet.getString("password"),
            resultSet.getString("name"),
            resultSet.getString("email")
    );

    private final JdbcTemplate jdbcTemplate;

    public DbUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (user_id, password, name, email) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET password = ?, name = ?, email = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getId());
    }

    @Override
    public Optional<User> findById(Long pk) {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = jdbcTemplate.queryForObject(sql, USER_ROW_MAPPER, pk);
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, USER_ROW_MAPPER);
    }

    @Override
    public void delete(User target) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, target.getId());
    }

    @Override
    public Optional<User> findByUserId(String username) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        User user = jdbcTemplate.queryForObject(sql, USER_ROW_MAPPER, username);
        return Optional.ofNullable(user);
    }
}
