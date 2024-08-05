package org.example.jspcafe.comment.repository;

import org.example.jspcafe.Component;
import org.example.jspcafe.DatabaseConnectionManager;
import org.example.jspcafe.ReflectionIdFieldExtractor;
import org.example.jspcafe.comment.model.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JdbcCommentRepository extends ReflectionIdFieldExtractor<Comment> implements CommentRepository  {

    private final DatabaseConnectionManager connectionManager;

    private static final String SOFT_DELETE_SQL = "UPDATE comments SET deleted_at = NOW() WHERE comment_id = ?";

    public JdbcCommentRepository(DatabaseConnectionManager connectionManager) {
        super(Comment.class);
        this.connectionManager = connectionManager;
    }

    @Override
    public Comment save(Comment comment) {
        validateComment(comment);

        if (comment.getCommentId() != null) {
            update(comment);
            return comment;
        }

        String sql = "INSERT INTO comments (post_id, user_id, content, created_at) VALUES (?, ?, ?, ?)";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, comment.getPostId());
            pstmt.setLong(2, comment.getUserId());
            pstmt.setString(3, comment.getContent().getValue());
            pstmt.setTimestamp(4, Timestamp.valueOf(comment.getCreatedAt()));
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return putId(generatedKeys.getLong(1), comment);
                } else {
                    throw new SQLException("Creating comment failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Comment 저장 중 오류 발생", e);
        }
    }

    @Override
    public Optional<Comment> findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID는 null일 수 없습니다.");
        }

        String sql = "SELECT * FROM comments WHERE comment_id = ? AND deleted_at IS NULL";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToComment(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Comment 조회 중 오류 발생", e);
        }

        return Optional.empty();
    }

    @Override
    public void delete(Comment comment) {
        validateComment(comment);

        if (comment.getCommentId() == null) {
            throw new IllegalArgumentException("ID는 null일 수 없습니다.");
        }

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SOFT_DELETE_SQL)) {

            pstmt.setLong(1, comment.getCommentId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Comment 삭제 중 오류 발생", e);
        }

    }

    @Override
    public void update(Comment comment) {
        validateComment(comment);

        if (comment.getCommentId() == null) {
            throw new IllegalArgumentException("ID는 null일 수 없습니다.");
        }

        String sql = "UPDATE comments SET post_id = ?, user_id = ?, content = ?, created_at = ? WHERE comment_id = ? AND deleted_at IS NULL";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, comment.getPostId());
            pstmt.setLong(2, comment.getUserId());
            pstmt.setString(3, comment.getContent().getValue());
            pstmt.setTimestamp(4, Timestamp.valueOf(comment.getCreatedAt()));
            pstmt.setLong(5, comment.getCommentId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Comment 수정 중 오류 발생", e);
        }
    }

    private void validateComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("Entity는 null일 수 없습니다.");
        }
    }

//    @Override
//    public List<CommentVO> findAllByPostIdsJoinFetch(List<Long> postIds) {
//        if (postIds.isEmpty()) {
//            return List.of();
//        }
//
//        String placeholders = postIds.stream()
//                .map(id -> "?")
//                .collect(Collectors.joining(","));
//        String sql = "SELECT c.comment_id, c.post_id, c.user_id, u.nickname, c.content, c.created_at " +
//                "FROM comments c " +
//                "JOIN users u ON c.user_id = u.user_id " +
//                "WHERE c.post_id IN (" + placeholders + ") " +
//                "AND c.deleted_at IS NULL";
//
//        try (Connection conn = connectionManager.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            for (int i = 0; i < postIds.size(); i++) {
//                pstmt.setLong(i + 1, postIds.get(i));
//            }
//
//            try (ResultSet rs = pstmt.executeQuery()) {
//                return mapResultSetToEntities(rs);
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException("Comment 조회 중 오류 발생", e);
//        }
//    }


    private List<CommentVO> mapResultSetToEntities(ResultSet rs) throws SQLException {
        List<CommentVO> comments = new ArrayList<>();
        while (rs.next()) {
            CommentVO comment = new CommentVO(
                    rs.getLong("comment_id"),
                    rs.getLong("post_id"),
                    rs.getLong("user_id"),
                    rs.getString("nickname"),
                    rs.getString("content"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
            comments.add(comment);
        }
        return comments;
    }
    @Override
    public void deleteAllInBatch() {
        try (Connection conn = connectionManager.getConnection();
             Statement statement = conn.createStatement()) {
            statement.execute("DELETE FROM comments");
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting all comments", e);
        }
    }

    @Override
    public List<CommentVO> findCommentsJoinUser(Long postId) {
        String sql = "SELECT c.comment_id, c.post_id, c.user_id, u.nickname, c.content, c.created_at " +
                "FROM comments c " +
                "JOIN users u ON c.user_id = u.user_id " +
                "WHERE c.post_id = ? AND c.deleted_at IS NULL";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, postId);

            try (ResultSet rs = pstmt.executeQuery()) {
                return mapResultSetToEntities(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Comment 조회 중 오류 발생", e);
        }
    }

    @Override
    public int count(Long postId) {
        String sql = "SELECT COUNT(*) " +
                "FROM comments " +
                "WHERE post_id = ? AND deleted_at IS NULL";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, postId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("댓글 수 조회 중 오류 발생", e);
        }
    }

    @Override
    public List<Comment> findAllByPostId(Long postId) {
        String sql = "SELECT * FROM comments WHERE post_id = ? AND deleted_at IS NULL";

        try (Connection conn = connectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, postId);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<Comment> comments = new ArrayList<>();
                while (rs.next()) {
                    comments.add(mapRowToComment(rs));
                }
                return comments;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Comment 조회 중 오류 발생", e);
        }
    }

    private Comment mapRowToComment(ResultSet rs) throws SQLException {
        Comment comment = new Comment(
                rs.getLong("post_id"),
                rs.getLong("user_id"),
                rs.getString("content"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
        return putId(rs.getLong("comment_id"), comment);
    }
}
