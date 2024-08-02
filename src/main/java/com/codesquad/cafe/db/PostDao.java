package com.codesquad.cafe.db;

import com.codesquad.cafe.db.domain.Post;
import com.codesquad.cafe.db.domain.PostDetail;
import com.codesquad.cafe.db.domain.PostWithAuthor;
import com.codesquad.cafe.db.rowmapper.PostDetailRowMapper;
import com.codesquad.cafe.db.rowmapper.PostRowMapper;
import com.codesquad.cafe.db.rowmapper.PostWithAuthorRowMapper;
import com.codesquad.cafe.db.rowmapper.RowMapper;
import com.codesquad.cafe.exception.DBException;
import com.codesquad.cafe.exception.NoSuchDataException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostDao implements PostRepository {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final JdbcTemplate jdbcTemplate;

    private final PostRowMapper postRowMapper;

    private final PostWithAuthorRowMapper postWithAuthorRowMapper;

    private final PostDetailRowMapper postDetailRowMapper;

    public PostDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.postRowMapper = new PostRowMapper();
        this.postWithAuthorRowMapper = new PostWithAuthorRowMapper();
        this.postDetailRowMapper = new PostDetailRowMapper();
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == null) {
            return create(post);
        } else {
            return update(post);
        }
    }

    public Post create(Post post) {
        String sql = "INSERT INTO `post` (author_id, title, content, view, filename, created_at, updated_at, deleted) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        Long id = jdbcTemplate.saveAndGetGeneratedKey(sql, (ps) -> {
            try {
                ps.setLong(1, post.getAuthorId());
                ps.setString(2, post.getTitle());
                ps.setString(3, post.getContent());
                ps.setInt(4, post.getView());
                ps.setString(5, post.getFileName());
                ps.setTimestamp(6, Timestamp.valueOf(post.getCreatedAt()));
                ps.setTimestamp(7, Timestamp.valueOf(post.getUpdatedAt()));
                ps.setBoolean(8, post.isDeleted());
            } catch (SQLException e) {
                log.warn("error while prepare statement : {}", sql);
                throw new DBException("fail to prepare statement PostDao.save");
            }
        });
        post.setId(id);
        return findById(post.getId()).get();
    }

    public Post update(Post post) {
        if (!existsById(post.getId())) {
            log.warn("no such post : {}", post.getId());
            throw new NoSuchDataException("no such post :" + post.getId());
        }

        String UPDATE_SQL = "UPDATE `post` SET "
                + "author_id = ?, "
                + "title = ?, "
                + "content = ?, "
                + "filename = ?, "
                + "view = ?, "
                + "created_at = ?, "
                + "updated_at = ?, "
                + "deleted = ? "
                + "WHERE id = ?";

        int affected = jdbcTemplate.executeUpdate(UPDATE_SQL, ps -> {
            try {
                ps.setLong(1, post.getAuthorId());
                ps.setString(2, post.getTitle());
                ps.setString(3, post.getContent());
                ps.setString(4, post.getFileName());
                ps.setInt(5, post.getView());
                ps.setTimestamp(6, Timestamp.valueOf(post.getCreatedAt()));
                ps.setTimestamp(7, Timestamp.valueOf(post.getUpdatedAt()));
                ps.setBoolean(8, post.isDeleted());
                ps.setLong(9, post.getId());
            } catch (SQLException e) {
                log.warn("error while prepare statement : {}", UPDATE_SQL);
                throw new DBException("fail to prepare statement PostDao.update");
            }
        });

        if (affected != 1) {
            log.warn("update failed");
            throw new DBException("update failed");
        }

        return findById(post.getId()).get();
    }

    public void updateDeleted(Long id) {
        LocalDateTime now = LocalDateTime.now();
        Map<String, Consumer<PreparedStatement>> sqls = new HashMap<>();
        sqls.put("UPDATE `comment` SET deleted = true, updated_at = ? WHERE post_id = ?", ps -> {
            try {
                ps.setTimestamp(1, Timestamp.valueOf(now));
                ps.setLong(2, id);
            } catch (SQLException e) {
                log.warn("error while prepare statement update deleted comment");
                throw new DBException("fail to prepare statement PostDao.updateDeleted");
            }
        });
        sqls.put("UPDATE `post` SET deleted = true, updated_at = ? WHERE id = ?", ps -> {
            try {
                ps.setTimestamp(1, Timestamp.valueOf(now));
                ps.setLong(2, id);
            } catch (SQLException e) {
                log.warn("error while prepare statement update deleted post");
                throw new DBException("fail to prepare statement PostDao.updateDeleted");
            }
        });

        jdbcTemplate.executeUpdateInTransaction(sqls);
    }


    @Override
    public Optional<Post> findById(Long id) {
        String sql = "SELECT * FROM `post` WHERE id = ?";
        Post post = jdbcTemplate.queryForObject(sql, ps -> {
            try {
                ps.setLong(1, id);
            } catch (SQLException exception) {
                log.warn("error while prepare statement : {}", sql);
                throw new DBException("fail to prepare statement PostDao.findById");
            }
        }, postRowMapper);
        return Optional.ofNullable(post);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) > 0 FROM post WHERE id = ?";
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

    @Override
    public List<Post> findAll() {
        String sql = "SELECT * FROM `post`";
        return jdbcTemplate.queryForList(
                sql,
                ps -> {
                },
                postRowMapper);
    }

    @Override
    public Page<PostWithAuthor> findPostWithAuthorByPageSortByCreatedAtDesc(int pageNum, int pageSize) {
        if (pageNum < 1 || pageSize < 1) {
            throw new IllegalArgumentException("page num and page size should be greater than 0");
        }

        int totalElements = countAll();
        String sql = "SELECT "
                + "p.id as p_id, "
                + "p.title as p_title, "
                + "p.content as p_content, "
                + "p.filename as p_filename, "
                + "p.view as p_view, "
                + "p.author_id as p_author_id, "
                + "u.username as u_username, "
                + "p.created_at as p_created_at, "
                + "p.updated_at as p_updated_at, "
                + "p.deleted as p_deleted "
                + "FROM `post` p LEFT JOIN `user` u ON p.author_id = u.id "
                + "WHERE p.deleted = false "
                + "ORDER BY p.created_at DESC "
                + "LIMIT ? OFFSET ?";
        List<PostWithAuthor> posts = jdbcTemplate.queryForList(
                sql,
                ps -> {
                    try {
                        ps.setInt(1, pageSize);
                        ps.setInt(2, (pageNum - 1) * pageSize);
                    } catch (SQLException e) {
                        log.warn("error while prepare statement : {}", sql);
                        throw new DBException("fail to prepare statement PostDao.PostDetailsDto");
                    }
                }, postWithAuthorRowMapper);
        return Page.of(posts, pageNum, pageSize, totalElements);
    }


    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM `post` WHERE id = ?";
        jdbcTemplate.executeUpdate(sql, ps -> {
            try {
                ps.setLong(1, id);
            } catch (SQLException e) {
                log.warn("error while prepare statement : {}", sql);
                throw new DBException("fail to prepare statement PostDao.deleteById");
            }
        });
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM `post`";
        jdbcTemplate.executeUpdate(sql, ps -> {
        });
    }

    @Override
    public int countAll() {
        String sql = "SELECT count(*) FROM `post`";
        return jdbcTemplate.queryForObject(sql, ps -> {
        }, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs) throws SQLException {
                return rs.getInt(1);
            }
        });
    }

    @Override
    public Optional<PostWithAuthor> findPostWithAuthorById(Long id) {
        String sql = "SELECT "
                + "p.id as p_id, "
                + "p.title as p_title, "
                + "p.content as p_content, "
                + "p.filename as p_filename, "
                + "p.view as p_view, "
                + "p.author_id as p_author_id, "
                + "u.username as u_username, "
                + "p.created_at as p_created_at, "
                + "p.updated_at as p_updated_at, "
                + "p.deleted as p_deleted "
                + "FROM `post` p LEFT JOIN `user` u ON p.author_id = u.id "
                + "WHERE p.id = ?";
        PostWithAuthor post = jdbcTemplate.queryForObject(
                sql,
                ps -> {
                    try {
                        ps.setLong(1, id);
                    } catch (SQLException e) {
                        log.warn("error while prepare statement : {}", sql);
                        throw new DBException("fail to prepare statement PostDao.findPostWithAuthorById");
                    }
                }, postWithAuthorRowMapper);
        return Optional.ofNullable(post);
    }

    @Override
    public Optional<PostDetail> findPostDetailById(Long id) {
        String sql = "SELECT "
                + "p.id as p_id, "
                + "p.title as p_title, "
                + "p.content as p_content, "
                + "p.filename as p_filename, "
                + "p.view as p_view, "
                + "p.author_id as p_author_id, "
                + "u.username as u_username, "
                + "p.created_at as p_created_at, "
                + "p.updated_at as p_updated_at, "
                + "p.deleted as p_deleted, "
                + "c.id as c_id, "
                + "c.post_id as c_post_id, "
                + "c.parent_id as c_parent_id, "
                + "c.user_id as c_user_id, "
                + "c.content as c_content, "
                + "c.created_at as c_created_at, "
                + "c.updated_at as c_updated_at, "
                + "c.deleted as c_deleted, "
                + "cu.username as cu_username "
                + "FROM `post` p LEFT JOIN `user` u ON p.author_id = u.id AND p.deleted = false "
                + "LEFT JOIN `comment` c ON p.id = c.post_id AND c.deleted = false "
                + "LEFT JOIN `user` cu ON c.user_id = cu.id "
                + "WHERE p.id = ? "
                + "ORDER BY coalesce(parent_id, c.id), c.created_at";
        PostDetail post = jdbcTemplate.queryForObject(
                sql,
                ps -> {
                    try {
                        ps.setLong(1, id);
                    } catch (SQLException e) {
                        log.warn("error while prepare statement : {}", sql);
                        throw new DBException("fail to prepare statement PostDao.findPostWithAuthorById");
                    }
                }, postDetailRowMapper);
        return Optional.ofNullable(post);
    }

    @Override
    public void addView(Post post) {
        if (post == null || post.getId() == null) {
            return;
        }
        String UPDATE_VIEW_SQL = "UPDATE `post` SET view = view + 1 WHERE id = ?";
        int affected = jdbcTemplate.executeUpdate(UPDATE_VIEW_SQL, ps -> {
            try {
                ps.setLong(1, post.getId());
            } catch (SQLException e) {
                log.warn("error while prepare statement : {}", UPDATE_VIEW_SQL);
                throw new DBException("fail to prepare statement PostDao.addView");
            }
        });
        if (affected == 1) {
            post.addView();
        }
    }

}

