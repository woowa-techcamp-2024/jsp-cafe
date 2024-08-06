package org.example.cafe.infrastructure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.example.cafe.domain.User;
import org.example.cafe.domain.UserRepository;
import org.example.cafe.infrastructure.jdbc.JdbcTemplate;
import org.example.cafe.infrastructure.jdbc.RowMapper;

public class UserJdbcRepository implements UserRepository {

    private static final String INSERT = "INSERT INTO `USER` (user_id, password, nickname, email) VALUES (?, ?, ?, ?)";
    private static final String SELECT = "SELECT * FROM `USER`";
    private static final String SELECT_BY_ID = "SELECT * FROM `USER` WHERE user_id = ?";
    private static final String DELETE = "DELETE FROM `USER`";
    private static final String UPDATE = "UPDATE `USER` SET password=?, nickname=?, email=? where user_id=?";

    private static final UserRowMapper userRowMapper = new UserRowMapper();
    private final JdbcTemplate jdbcTemplate;

    public UserJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(User user) {
        jdbcTemplate.update(INSERT, null, user.getUserId(), user.getPassword(), user.getNickname(), user.getEmail());
    }

    public void update(User user) {
        jdbcTemplate.update(UPDATE, null, user.getPassword(), user.getNickname(), user.getEmail(), user.getUserId());
    }

    public User findById(String id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID, userRowMapper, id);
    }

    public List<User> findAll() {
        return jdbcTemplate.query(SELECT, userRowMapper);
    }

    public void deleteAll() {
        jdbcTemplate.update(DELETE, null);
    }

    static class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getString("user_id"),
                    rs.getString("password"),
                    rs.getString("nickname"),
                    rs.getString("email")
            );
        }
    }
}
