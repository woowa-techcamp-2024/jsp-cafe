package com.codesquad.cafe.db;

import com.codesquad.cafe.db.domain.User;
import com.codesquad.cafe.db.rowmapper.UserRowMapper;
import com.codesquad.cafe.exception.DBException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDao implements UserRepository {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final JdbcTemplate jdbcTemplate;

    private final UserRowMapper userRowMapper;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = new UserRowMapper();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            return create(user);
        } else {
            return update(user);
        }
    }

    private User create(User user) {
        if (user.getId() != null) {
            throw new IllegalArgumentException("이미 존재하는 user 입니다.");
        }
        findByUsername(user.getUsername()).ifPresent(existUser -> {
            log.debug("unique constraint violated: {}", user.getUsername());
            throw new IllegalArgumentException("이미 존재하는 username 입니다.");
        });
        String sql = "INSERT INTO `user` (username, password, name, email, created_at, updated_at, deleted) VALUES(?, ?, ?, ?, ?, ?, ?)";
        Long id = jdbcTemplate.saveAndGetGeneratedKey(sql, pstmt -> {
            try {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
                pstmt.setTimestamp(5, Timestamp.valueOf(user.getCreatedAt()));
                pstmt.setTimestamp(6, Timestamp.valueOf(user.getUpdatedAt()));
                pstmt.setBoolean(7, user.isDeleted());
            } catch (SQLException e) {
                log.warn("error while prepare statement : {}", sql);
                throw new DBException("fail to prepare statement UserDao.create");
            }
        });
        user.setId(id);
        return findById(id).get();
    }

    private User update(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("존재하지 않는 user 입니다.");
        }

        String sql = "UPDATE `user` SET "
                + "username = ?, "
                + "password = ?, "
                + "name = ?, "
                + "email = ?, "
                + "created_at = ?, "
                + "updated_at = ?, "
                + "deleted = ? "
                + "WHERE id = ?";
        int affected = jdbcTemplate.executeUpdate(sql, pstmt -> {
            try {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
                pstmt.setTimestamp(5, Timestamp.valueOf(user.getCreatedAt()));
                pstmt.setTimestamp(6, Timestamp.valueOf(user.getUpdatedAt()));
                pstmt.setBoolean(7, user.isDeleted());
                pstmt.setLong(8, user.getId());
            } catch (SQLException e) {
                log.warn("error while prepare statement : {}", sql);
                throw new DBException("fail to prepare statement UserDao.update");
            }
        });
        if (affected == 0) {
            log.warn("update failed : {}", sql);
            throw new DBException("update failed");
        }
        return user;
    }


    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * from `user` WHERE id = ?";
        User user = jdbcTemplate.queryForObject(sql, pstmt -> {
            try {
                pstmt.setLong(1, id);
            } catch (SQLException sqlException) {
                log.warn("error while prepare statement : {}", sql);
                throw new DBException("fail to prepare statement UserDao.findById");
            }
        }, userRowMapper);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * from `user` WHERE username = ?";
        User user = jdbcTemplate.queryForObject(sql, pstmt -> {
            try {
                pstmt.setString(1, username);
            } catch (SQLException sqlException) {
                log.warn("error while prepare statement : {}", sql);
                throw new DBException("fail to prepare statement UserDao.findByUsername");
            }
        }, userRowMapper);
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * from `user`";
        return jdbcTemplate.queryForList(sql, pstmt -> {
        }, userRowMapper);
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM `user`";
        jdbcTemplate.executeUpdate(sql, pstmt -> {
        });
    }
}
