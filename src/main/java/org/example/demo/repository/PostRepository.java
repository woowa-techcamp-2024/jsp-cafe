package org.example.demo.repository;

import org.example.demo.db.DbConfig;
import org.example.demo.domain.Comment;
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
    private static final Logger logger = LoggerFactory.getLogger(PostRepository.class);
    private DbConfig dbConfig;

    public PostRepository(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public Optional<Post> getPost(Long postId) {
        String sql = "SELECT p.*, u.user_id, u.name, c.id AS comment_id, c.contents AS comment_contents, c.created_at AS comment_created_at\n" +
                "FROM posts p\n" +
                "JOIN users u ON p.writer_id = u.id\n" +
                "LEFT JOIN comments c ON p.id = c.post_id\n" +
                "WHERE p.id = ?\n" +
                "AND c.is_present = true;";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, postId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(createPostWithCommentsFromResultSet(rs));
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

            pstmt.setLong(1, dao.getWriter());
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
        String sql = "SELECT p.*, u.user_id, u.name FROM posts p JOIN `users` u ON p.writer_id = u.id WHERE p.is_present = true";

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
                rs.getTimestamp("created_at").toLocalDateTime(),
                null
        );
    }

    private Post createPostWithCommentsFromResultSet(ResultSet rs) throws SQLException {
        User writer = new User(
                rs.getLong("writer_id"),
                rs.getString("user_id"),
                null,
                rs.getString("name"),
                null
        );

        Long postId = rs.getLong("id");
        List<Comment> comments = new ArrayList<>();

        // Post 정보 설정
        Post post = new Post(
                postId,
                writer,
                rs.getString("title"),
                rs.getString("contents"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                comments
        );

        // Comment 정보 설정
        do {
            Long commentId = rs.getLong("comment_id");
            if (commentId != 0) {
                Comment comment = new Comment(
                        commentId,
                        postId,
                        writer,
                        rs.getString("comment_contents"),
                        rs.getTimestamp("comment_created_at").toLocalDateTime()
                );
                comments.add(comment);
            }
        } while (rs.next());

        return post;
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
        String postSql = "UPDATE posts SET is_present = false WHERE id = ?";
        String commentSql = "UPDATE comments SET is_present = false WHERE post_id = ?";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(postSql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting post", e);
            e.printStackTrace();
        }

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(commentSql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting comments", e);
            e.printStackTrace();
        }
    }
}