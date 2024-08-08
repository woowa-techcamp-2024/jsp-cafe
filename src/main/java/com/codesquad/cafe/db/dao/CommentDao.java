package com.codesquad.cafe.db.dao;

import com.codesquad.cafe.db.domain.Comment;
import com.codesquad.cafe.db.rowmapper.CommentRowMapper;
import com.codesquad.cafe.db.rowmapper.CommentWithUserRowMapper;
import com.codesquad.cafe.db.rowmapper.RowMapper;
import com.codesquad.cafe.exception.DBException;
import com.codesquad.cafe.exception.NoSuchDataException;
import com.codesquad.cafe.model.aggregate.CommentWithUser;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
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
                throw new DBException("fail to prepare statement CommentDao.save");
            }
        });
        return findById(id).get();
    }

    public Comment update(Comment comment) {
        if (!existsById(comment.getId())) {
            log.warn("no such comment : {}", comment.getId());
            throw new NoSuchDataException("no such comment :" + comment.getId());
        }

        String sql = "UPDATE `comment` SET "
                + "post_id = ?, "
                + "parent_id = ?, "
                + "user_id = ?, "
                + "content = ?, "
                + "created_at = ?, "
                + "updated_at = ?, "
                + "deleted = ? "
                + "WHERE id = ?";

        jdbcTemplate.executeUpdate(sql, ps -> {
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
                log.warn("error while prepare statement : {}", sql);
                throw new DBException("fail to prepare statement CommentDao.update");
            }
        });

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

    public Comment softDelete(Long id) {
        if (!existsById(id)) {
            log.warn("no such comment : {}", id);
            throw new NoSuchDataException("no such comment :" + id);
        }

        String sql = "UPDATE `comment` SET deleted = true, updated_at = ? WHERE id = ?";
        jdbcTemplate.executeUpdate(sql, ps -> {
            try {
                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                ps.setLong(2, id);
            } catch (SQLException e) {
                log.warn("error while prepare statement : {}", sql);
                throw new DBException("fail to prepare statement PostDao.update");
            }
        });

        return findById(id).get();
    }

    public List<CommentWithUser> findCommentsByPostId(Long postId) {
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
                + "FROM `comment` c LEFT JOIN `user` u on c.user_id = u.id "
                + "WHERE c.post_id = ? AND deleted = false"
                + "ORDER BY c.created_at DESC";
        return jdbcTemplate.queryForList(sql, ps -> {
            try {
                ps.setLong(1, postId);
            } catch (SQLException exception) {
                log.warn("error while prepare statement : {}", sql);
                throw new DBException("fail to prepare statement CommentDao.findCommentsByPostId");
            }
        }, commentWithUserRowMapper);
    }

    public List<CommentWithUser> findNoOffsetCommentsByPostId(Long postId, Long lastCommentId, int limit) {
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
                + "FROM `comment` c LEFT JOIN `user` u on c.user_id = u.id ";
        if (lastCommentId != null) {
            sql += "WHERE c.post_id = ? ";
        }
        sql += "AND deleted = false"
                + "ORDER BY c.created_at DESC LIMIT ?";
        return jdbcTemplate.queryForList(sql, ps -> {
            try {
                ps.setLong(1, postId);
                ps.setLong(2, lastCommentId);
                ps.setLong(3, limit);
            } catch (SQLException exception) {
                log.warn("error while prepare statement");
                throw new DBException("fail to prepare statement CommentDao.findCommentsByPostId");
            }
        }, commentWithUserRowMapper);
    }


    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) > 0 FROM comment WHERE id = ? AND deleted = false";
        return jdbcTemplate.queryForObject(sql, ps -> {
            try {
                ps.setLong(1, id);
            } catch (SQLException e) {
                log.warn("error while prepare statement : {}", sql);
                throw new DBException("fail to prepare statement CommentDao.existsById");
            }
        }, new RowMapper<Boolean>() {
            @Override
            public Boolean mapRow(ResultSet rs) throws SQLException {
                return rs.getBoolean(1);
            }
        });
    }

}

