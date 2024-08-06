package org.example.post.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.post.model.PostStatus;
import org.example.post.model.dao.Post;
import org.example.post.model.dto.PostDto;
import org.example.reply.model.ReplyStatus;
import org.example.util.DatabaseConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PostRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostRepository.class);
    private DatabaseConnectionPool connectionPool;

    @Autowired
    public PostRepository(DatabaseConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void save(Post post) throws SQLException {
        logger.info("Saving post: {}", post);
        String sql = "insert into posts (user_id, title, contents, status, created_at) values (?, ?, ?, ?, ?)";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, post.getUserId());
            ps.setString(2, post.getTitle());
            ps.setString(3, post.getContents());
            ps.setString(4, post.getPostStatus().name());
            ps.setObject(5, post.getCreatedAt());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error saving post", e);
            throw new SQLException(e);
        }
    }

    public PostDto findById(Long id) throws SQLException {
        String sql = "SELECT p.id, p.user_id, u.name as username, p.title, p.contents, p.status, p.created_at " +
                "FROM posts p, users u " +
                "WHERE p.id = ? AND p.status = ?";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.setString(2, PostStatus.AVAILABLE.name());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new PostDto.Builder()
                            .id(rs.getLong("id"))
                            .userId(rs.getString("user_id"))
                            .username(rs.getString("username"))
                            .title(rs.getString("title"))
                            .contents(rs.getString("contents"))
                            .status(PostStatus.valueOf(rs.getString("status")))
                            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                            .build();
                }
            }
        }
        throw new SQLException("게시물을 찾을 수 없습니다.");
    }

    public List<PostDto> findAll() throws SQLException {
        String sql = "SELECT p.id, p.user_id, u.name as username, p.title, p.contents, p.status, p.created_at FROM posts p, users u WHERE status = ? AND p.user_id = u.user_id";
        List<PostDto> posts = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, PostStatus.AVAILABLE.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PostDto post = new PostDto.Builder()
                            .id(rs.getLong("id"))
                            .userId(rs.getString("user_id"))
                            .username(rs.getString("username"))
                            .title(rs.getString("title"))
                            .contents(rs.getString("contents"))
                            .status(PostStatus.valueOf(rs.getString("status")))
                            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                            .build();
                    posts.add(post);
                }
            }
            return posts;
        } catch (SQLException e) {
            throw new SQLException("데이터베이스에서 게시물을 조회하는 중 오류가 발생했습니다.", e);
        }
    }

    public List<PostDto> findAllWithPagination(LocalDateTime cursorTimestamp, Long cursorId, int limit)
            throws SQLException {
        String sql = "SELECT p.id, p.user_id, u.name as username, p.title, p.contents, p.status, p.created_at " +
                "FROM posts p, users u " +
                "WHERE p.status = ? AND p.user_id = u.user_id " +
                "AND (p.created_at < ? OR (p.created_at = ? AND p.id < ?)) " +
                "ORDER BY p.created_at DESC, p.id DESC " +
                "LIMIT ?";

        List<PostDto> posts = new ArrayList<>();
        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, PostStatus.AVAILABLE.name());
            ps.setTimestamp(2, Timestamp.valueOf(cursorTimestamp));
            ps.setTimestamp(3, Timestamp.valueOf(cursorTimestamp));
            ps.setLong(4, cursorId);
            ps.setInt(5, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PostDto post = new PostDto.Builder()
                            .id(rs.getLong("id"))
                            .userId(rs.getString("user_id"))
                            .username(rs.getString("username"))
                            .title(rs.getString("title"))
                            .contents(rs.getString("contents"))
                            .status(PostStatus.valueOf(rs.getString("status")))
                            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                            .cursorId(rs.getLong("id"))
                            .cursorTimestamp(rs.getTimestamp("created_at").toLocalDateTime())
                            .build();
                    posts.add(post);
                }
            }
            return posts;
        } catch (SQLException e) {
            throw new SQLException("데이터베이스에서 게시물을 조회하는 중 오류가 발생했습니다.");
        }
    }

    public Post update(Post post) throws SQLException {
        logger.info("Updating post: {}", post);
        String sql = "UPDATE posts SET title = ?, contents = ? WHERE id = ?";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContents());
            ps.setLong(3, post.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating post failed, no rows affected.");
            }

            return post;
        } catch (SQLException e) {
            logger.error("Error updating post", e);
            throw new SQLException(e);
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "UPDATE posts SET status = ? WHERE id = ?";

        try (Connection conn = connectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, PostStatus.DELETED.name());
            ps.setLong(2, id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating post failed, no rows affected.");
            }
        } catch (SQLException e) {
            logger.error("Error updating post", e);
            throw new SQLException(e);
        }
    }

    public void softDeletePostAndReplies(Long postId) throws SQLException {
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            conn.setAutoCommit(false);

            softDeletePost(conn, postId);
            softDeleteReplies(conn, postId);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    logger.error("Error rolling back transaction: " + rollbackEx.getMessage());
                }
            }
            logger.error("Error soft deleting post and replies: " + e.getMessage());
            throw new SQLException("Failed to soft delete post and replies", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection: " + e.getMessage());
                }
            }
        }
    }

    private void softDeletePost(Connection conn, Long postId) throws SQLException {
        String sql = "UPDATE posts SET status = ? WHERE id = ?";
        executeUpdate(conn, sql, PostStatus.DELETED.name(), postId);
    }

    private void softDeleteReplies(Connection conn, Long postId) throws SQLException {
        String sql = "UPDATE replies SET status = ? WHERE post_id = ?";
        executeUpdate(conn, sql, ReplyStatus.DELETED.name(), postId);
    }

    private void executeUpdate(Connection conn, String sql, String status, Long id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, id);
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating failed, no rows affected.");
            }
        }
    }
}
