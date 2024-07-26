package woopaca.jspcafe.repository;

import woopaca.jspcafe.database.JdbcTemplate;
import woopaca.jspcafe.model.User;

import java.util.List;
import java.util.Optional;

public class UserMySQLRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserMySQLRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        String sql = "INSERT INTO user (username, nickname, password, created_at) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getNickname(), user.getPassword(), user.getCreatedAt());
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM user";
        return jdbcTemplate.queryForList(sql, User.class);
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, User.class, id));

    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, User.class, username));
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        String sql = "SELECT * FROM user WHERE nickname = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, User.class, nickname));
    }
}
