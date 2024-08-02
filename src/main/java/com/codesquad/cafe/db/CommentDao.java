package com.codesquad.cafe.db;

import com.codesquad.cafe.db.domain.Comment;
import com.codesquad.cafe.db.domain.CommentWithUser;
import com.codesquad.cafe.db.rowmapper.CommentRowMapper;
import com.codesquad.cafe.db.rowmapper.CommentWithUserRowMapper;
import com.codesquad.cafe.db.rowmapper.RowMapper;
import com.codesquad.cafe.exception.DBException;
import com.codesquad.cafe.exception.NoSuchDataException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommentDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final JdbcTemplate jdbcTemplate;

    private final CommentRowMapper commentRowMapper;

    private final CommentWithUserRowMapper commentWithUserRowMapper;

    public CommentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.commentRowMapper = new CommentRowMapper();
        this.commentWithUserRowMapper = new CommentWithUserRowMapper();
    }

    public Comment save(Comment comment) {
        if (comment.getId() == null) {
            return create(comment);
        } else {
            return update(comment);
        }
    }

    public Comment create(Comment comment) {
        String sql =
                "INSERT INTO comment (`post_id`, `parent_id`, `user_id`, `content`, `created_at`, `updated_at`, `deleted`) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?)";
        Long id = jdbcTemplate.saveAndGetGeneratedKey(sql, (ps) -> {
            try {
                ps.setLong(1, comment.getPostId());
                if (Objects.nonNull(comment.getParentId())) {
                    ps.setLong(2, comment.getParentId());
                } else {
                    ps.setNull(2, Types.BIGINT);
                }
                ps.setLong(3, comment.getUserId());
                ps.setString(4, comment.getContent());
                ps.setTimestamp(5, Timestamp.valueOf(comment.getCreatedAt()));
                ps.setTimestamp(6, Timestamp.valueOf(comment.getUpdatedAt()));
                ps.setBoolean(7, comment.isDeleted());
            } catch (SQLException e) {
                log.warn("error while prepare statement : {}", sql);
                throw new DBException("fail to prepare statement PostDao.save");
            }
        });
        comment.setId(id);
        return findById(comment.getId()).get();
    }

    public Comment update(Comment comment) {
        if (!existsById(comment.getId())) {
            log.warn("no such post : {}", comment.getId());
            throw new NoSuchDataException("no such post :" + comment.getId());
        }

        String UPDATE_SQL = "UPDATE `comment` SET "
                + "post_id = ?, "
                + "parent_id = ?, "
                + "user_id = ?, "
                + "content = ?, "
                + "created_at = ?, "
                + "updated_at = ?, "
                + "deleted = ? "
                + "WHERE id = ?";

        int affected = jdbcTemplate.executeUpdate(UPDATE_SQL, ps -> {
            try {
                ps.setLong(1, comment.getPostId());
                ps.setLong(2, comment.getParentId());
                ps.setLong(3, comment.getUserId());
                ps.setString(4, comment.getContent());
                ps.setTimestamp(5, Timestamp.valueOf(comment.getCreatedAt()));
                ps.setTimestamp(6, Timestamp.valueOf(comment.getUpdatedAt()));
                ps.setBoolean(7, comment.isDeleted());
                ps.setLong(8, comment.getId());
            } catch (SQLException e) {
                log.warn("error while prepare statement : {}", UPDATE_SQL);
                throw new DBException("fail to prepare statement PostDao.update");
            }
        });

        if (affected != 1) {
            log.warn("update failed");
            throw new DBException("update failed");
        }

        return findById(comment.getId()).get();
    }


    public Optional<Comment> findById(Long id) {
        String sql = "SELECT * FROM `comment` WHERE id = ?";
        Comment comment = jdbcTemplate.queryForObject(sql, ps -> {
            try {
                ps.setLong(1, id);
            } catch (SQLException exception) {
                log.warn("error while prepare statement : {}", sql);
                throw new DBException("fail to prepare statement PostDao.findById");
            }
        }, commentRowMapper);
        return Optional.ofNullable(comment);
    }

    public void updateDeletedByIdIn(List<Long> ids) {

    }


    public Optional<CommentWithUser> findCommentWithUserById(Long id) {
        String sql = "SELECT "
                + "c.id as c_id, "
                + "c.post_id as c_post_id, "
                + "c.parent_id as c_parent_id, "
                + "c.user_id as c_user_id, "
                + "u.username as u_username, "
                + "c.content as c_content, "
                + "c.created_at as c_created_at, "
                + "c.updated_at as c_updated_at, "
                + "c.deleted as c_deleted "
                + "FROM `comment` c LEFT JOIN `user` u on c.user_id = u.id WHERE c.id = ?";
        CommentWithUser comment = jdbcTemplate.queryForObject(sql, ps -> {
            try {
                ps.setLong(1, id);
            } catch (SQLException exception) {
                log.warn("error while prepare statement : {}", sql);
                throw new DBException("fail to prepare statement PostDao.findById");
            }
        }, commentWithUserRowMapper);
        return Optional.ofNullable(comment);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) > 0 FROM comment WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, ps -> {
            try {
                ps.setLong(1, id);
            } catch (SQLException e) {
                log.warn("error while prepare statement : {}", sql);
                throw new DBException("fail to prepare statement PostDao.existsById");
            }
        }, new RowMapper<Boolean>() {
            @Override
            public Boolean mapRow(ResultSet rs) throws SQLException {
                return rs.getBoolean(1);
            }
        });
    }

}

