package org.example.demo.repository;

import org.example.demo.WasInitializeListener;
import org.example.demo.db.DbConfig;
import org.example.demo.domain.Post;
import org.example.demo.domain.User;
import org.example.demo.model.PostCreateDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostRepository {
    private static PostRepository instance;
    private static final Logger logger = LoggerFactory.getLogger(PostRepository.class);

    private DbConfig dbConfig;
    private UserRepository userRepository;

    public PostRepository(DbConfig dbConfig, UserRepository userRepository) {
        this.dbConfig = dbConfig;
        this.userRepository = userRepository;
    }

    public Optional<Post> getPost(Long postId) {
        String sql = "SELECT p.*, u.user_id, u.name FROM posts p JOIN users u ON p.writer_id = u.id WHERE p.id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, postId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(createPostFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void addPost(PostCreateDao dao) {
        String sql = "INSERT INTO posts (writer_id, title, contents, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            User user = userRepository.getUserByUserId(dao.getWriter())
                    .orElseThrow(() -> new IllegalArgumentException("user not found"));

            pstmt.setLong(1, user.getId());
            pstmt.setString(2, dao.getTitle());
            pstmt.setString(3, dao.getContents());
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                long id = generatedKeys.getLong(1);
                logger.info("Generated Post ID: " + id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Post> getPosts() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT p.*, u.user_id, u.name FROM posts p JOIN `users` u ON p.writer_id = u.id ORDER BY p.created_at DESC";

        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                posts.add(createPostFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    private Post createPostFromResultSet(ResultSet rs) throws SQLException {
        User writer = new User(
                rs.getLong("writer_id"),
                rs.getString("user_id"),
                null, // password는 보안상 가져오지 않음
                rs.getString("name"),
                null  // email도 필요 없다면 가져오지 않음
        );

        return new Post(
                rs.getLong("id"),
                writer,
                rs.getString("title"),
                rs.getString("contents"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }

    public void updatePost(Long id, String title, String contents) {
        String sql = "UPDATE posts SET title = ?, contents = ? WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, contents);
            pstmt.setLong(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePost(Long id) {
        String sql = "DELETE FROM posts WHERE id = ?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}