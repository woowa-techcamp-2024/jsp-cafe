package com.codesquad.cafe.db;

import com.codesquad.cafe.db.entity.Post;
import com.codesquad.cafe.db.entity.PostDetailsDto;
import com.codesquad.cafe.db.rowmapper.PostDetailsDtoRowMapper;
import com.codesquad.cafe.db.rowmapper.PostRowMapper;
import com.codesquad.cafe.exception.DBException;
import com.codesquad.cafe.exception.NoSuchDataException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostDao implements PostRepository {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final JdbcTemplate jdbcTemplate;

    private final PostRowMapper postRowMapper;

    private final PostDetailsDtoRowMapper postDetailsDtoRowMapper;

    public PostDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.postRowMapper = new PostRowMapper();
        this.postDetailsDtoRowMapper = new PostDetailsDtoRowMapper();
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
        }, rs -> rs.getBoolean(1));
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
    public Page<PostDetailsDto> findPostWithAuthorByPageSortByCreatedAtDesc(int pageNum, int pageSize) {
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
                + "ORDER BY p.created_at DESC "
                + "LIMIT ? OFFSET ?";
        List<PostDetailsDto> posts = jdbcTemplate.queryForList(
                sql,
                ps -> {
                    try {
                        ps.setInt(1, pageSize);
                        ps.setInt(2, (pageNum - 1) * pageSize);
                    } catch (SQLException e) {
                        log.warn("error while prepare statement : {}", sql);
                        throw new DBException("fail to prepare statement PostDao.PostDetailsDto");
                    }
                }, postDetailsDtoRowMapper);
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
        }, rs -> rs.getInt(1));
    }

    @Override
    public Optional<PostDetailsDto> findPostWithAuthorById(Long id) {
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
        PostDetailsDto post = jdbcTemplate.queryForObject(
                sql,
                ps -> {
                    try {
                        ps.setLong(1, id);
                    } catch (SQLException e) {
                        log.warn("error while prepare statement : {}", sql);
                        throw new DBException("fail to prepare statement PostDao.findPostWithAuthorById");
                    }
                }, postDetailsDtoRowMapper);
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

