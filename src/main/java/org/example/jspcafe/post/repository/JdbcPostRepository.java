package org.example.jspcafe.post.repository;

import org.example.jspcafe.Component;
import org.example.jspcafe.DatabaseConnectionManager;
import org.example.jspcafe.ReflectionIdFieldExtractor;
import org.example.jspcafe.post.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcPostRepository extends ReflectionIdFieldExtractor<Post> implements PostRepository {

    private final DatabaseConnectionManager connectionManager;

    public JdbcPostRepository(DatabaseConnectionManager connectionManager) {
        super(Post.class);
        this.connectionManager = connectionManager;
    }

    @Override
    public Post save(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("Entity는 null일 수 없습니다.");
        }

        if (post.getPostId() != null) {
            update(post);
            return post;
        }
        String sql = "INSERT INTO posts (user_id, title, content, created_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, post.getUserId());
            pstmt.setString(2, post.getTitle().getValue());
            pstmt.setString(3, post.getContent().getValue());
            pstmt.setTimestamp(4, Timestamp.valueOf(post.getCreatedAt()));
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return putId(generatedKeys.getLong(1), post);
                } else {
                    throw new SQLException("Creating post failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Post 저장 중 오류 발생", e);
        }
    }

    @Override
    public Optional<Post> findById(Long id) {
        String sql = "SELECT * FROM posts WHERE post_id = ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToPost(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Post를 찾는 중 오류 발생", e);
        }

        return Optional.empty();
    }

    @Override
    public void delete(Post post) {
        if (post.getPostId() == null) {
            throw new IllegalArgumentException("Id는 null일 수 없습니다.");
        }
        String sql = "DELETE FROM posts WHERE post_id = ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, post.getPostId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting post", e);
        }
    }

    @Override
    public void update(Post post) {
        String sql = "UPDATE posts SET title = ?, content = ? WHERE post_id = ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, post.getTitle().getValue());
            pstmt.setString(2, post.getContent().getValue());
            if (post.getPostId() == null) {
                throw new IllegalArgumentException("Id는 null일 수 없습니다.");
            }
            pstmt.setLong(3, post.getPostId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating post", e);
        }
    }

    @Override
    public List<Post> findAll() {
        String sql = "SELECT * FROM posts";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            List<Post> posts = new ArrayList<>();
            while (rs.next()) {
                posts.add(mapRowToPost(rs));
            }
            return posts;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all posts", e);
        }
    }

    @Override
    public List<Post> findAll(int offset, int limit) {
        validateOffsetAndLimit(offset, limit);

        // Order by는 너무 비효율적이지만, 어쩔 수 없으니,,
        String sql = "SELECT * FROM posts " +
                "ORDER BY created_at DESC " +
                "LIMIT ? OFFSET ?";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            ResultSet rs = pstmt.executeQuery();

            List<Post> posts = new ArrayList<>();
            while (rs.next()) {
                posts.add(mapRowToPost(rs));
            }
            return posts;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding posts with pagination", e);
        }
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM posts";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new RuntimeException("Error counting posts");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error counting posts", e);
        }
    }

    @Override
    public void deleteAllInBatch() {
        String sql = "DELETE FROM posts";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all posts", e);
        }
    }

    private Post mapRowToPost(ResultSet rs) throws SQLException {
        Post post = new Post(
                rs.getLong("user_id"),
                rs.getString("title"),
                rs.getString("content"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
        return putId(rs.getLong("post_id"), post);
    }

    private void validateOffsetAndLimit(int offset, int limit) {
        if (offset < 0 || limit < 0) {
            throw new IllegalArgumentException("offset과 limit은 0 이상이어야 합니다.");
        }
    }
}
