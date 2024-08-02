package codesqaud.app.dao.user;

import codesqaud.app.db.JdbcTemplate;
import codesqaud.app.db.RowMapper;
import codesqaud.app.db.exception.DbConstraintException;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.*;

public class DbUserDao implements UserDao {
    private static final Logger log = LoggerFactory.getLogger(DbUserDao.class);

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
        if (user.getId() != null) {
            log.error("새로운 모델을 저장할 때 id를 명시적으로 지정하면 안됩니다.");
            throw new HttpException(SC_INTERNAL_SERVER_ERROR);
        }

        try {
            String sql = "INSERT INTO users (user_id, password, name, email) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
        } catch (DbConstraintException e) {
            throw new HttpException(SC_BAD_REQUEST, "id가 중복되었습니다.");
        }
    }

    @Override
    public void update(User user) {
        if (user.getId() == null) {
            log.error("업데이트할 모델에 id를 지정하지 않았습니다.");
            throw new HttpException(SC_INTERNAL_SERVER_ERROR);
        }

        String sql = "UPDATE users SET password = ?, name = ?, email = ? WHERE id = ?";
        int updateRow = jdbcTemplate.update(sql,
                user.getPassword(), user.getName(), user.getEmail(), user.getId());

        if (updateRow == 0) {
            throw new HttpException(SC_NOT_FOUND, "업데이트 할 qna 글을 찾지 못했습니다.");
        }
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
    public void delete(User user) {
        if(user.getId() == null) {
            log.error("삭제 할 모델에 id를 지정하지 않았습니다.");
            throw new HttpException(SC_INTERNAL_SERVER_ERROR);
        }

        String sql = "DELETE FROM users WHERE id = ?";
        int updateRow = jdbcTemplate.update(sql, user.getId());

        if (updateRow == 0) {
            throw new HttpException(SC_NOT_FOUND, "해당 사용자는 존재하지 않습니다.");
        }
    }

    @Override
    public Optional<User> findByUserId(String username) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        User user = jdbcTemplate.queryForObject(sql, USER_ROW_MAPPER, username);
        return Optional.ofNullable(user);
    }
}
