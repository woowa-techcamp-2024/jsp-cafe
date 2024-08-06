package org.example.post.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;
import org.example.post.model.PostStatus;
import org.example.post.model.dao.Post;
import org.example.post.model.dto.PostDto;
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
}
