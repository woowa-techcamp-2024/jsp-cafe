package com.jspcafe.board.model;

import com.jspcafe.util.DatabaseConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArticleDao {
    private final DatabaseConnector databaseConnector;

    public ArticleDao(final DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public void save(final Article article) {
        String sql = "INSERT INTO articles (id, user_id, title, nickname, content, create_at, update_at, is_deleted, deleted_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, article.id());
            pstmt.setString(2, article.userId());
            pstmt.setString(3, article.title());
            pstmt.setString(4, article.nickname());
            pstmt.setString(5, article.content());
            pstmt.setTimestamp(6, Timestamp.valueOf(article.createAt()));
            pstmt.setTimestamp(7, Timestamp.valueOf(article.updateAt()));
            pstmt.setBoolean(8, false);
            pstmt.setNull(9, Types.TIMESTAMP);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving article", e);
        }
    }

    public Optional<Article> findById(final String id) {
        String sql = "SELECT * FROM articles WHERE id = ? AND is_deleted = false";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Article(
                            rs.getString("id"),
                            rs.getString("user_id"),
                            rs.getString("title"),
                            rs.getString("nickname"),
                            rs.getString("content"),
                            rs.getTimestamp("create_at").toLocalDateTime(),
                            rs.getTimestamp("update_at").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding article by id", e);
        }
        return Optional.empty();
    }

    public List<Article> findAll() {
        String sql = "SELECT * FROM articles WHERE is_deleted = false ORDER BY update_at DESC";
        List<Article> articles = new ArrayList<>();
        try (Connection conn = databaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                articles.add(new Article(
                        rs.getString("id"),
                        rs.getString("user_id"),
                        rs.getString("title"),
                        rs.getString("nickname"),
                        rs.getString("content"),
                        rs.getTimestamp("create_at").toLocalDateTime(),
                        rs.getTimestamp("update_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all articles", e);
        }
        return articles;
    }

    public void update(final Article updateArticle) {
        String sql = "UPDATE articles SET title = ?, content = ?, update_at = ? WHERE id = ? AND user_id = ? AND is_deleted = false";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updateArticle.title());
            pstmt.setString(2, updateArticle.content());
            pstmt.setTimestamp(3, Timestamp.valueOf(updateArticle.updateAt()));
            pstmt.setString(4, updateArticle.id());
            pstmt.setString(5, updateArticle.userId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating article failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating article", e);
        }
    }

    public boolean delete(final String id, final String userId) {
        try (Connection conn = databaseConnector.getConnection()) {
            conn.setAutoCommit(false);
            try {
                if (!isArticleOwner(conn, id, userId)) {
                    return false; // 게시글 작성자가 아니면 삭제 불가
                }

                boolean hasReplies = hasReplies(conn, id);

                if (!hasReplies) {
                    // 댓글이 없는 경우, 게시글만 삭제
                    softDeleteArticle(conn, id);
                } else if (areAllRepliesByAuthor(conn, id, userId)) {
                    // 모든 댓글이 게시글 작성자의 것인 경우, 게시글과 댓글 모두 삭제
                    softDeleteArticleAndReplies(conn, id);
                } else {
                    // 댓글이 있고, 일부가 다른 사용자의 것인 경우 삭제 불가
                    return false;
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Error deleting article", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to database", e);
        }
    }

    private boolean isArticleOwner(Connection conn, String articleId, String userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM articles WHERE id = ? AND user_id = ? AND is_deleted = false";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, articleId);
            pstmt.setString(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private boolean hasReplies(Connection conn, String articleId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM replies WHERE article_id = ? AND is_deleted = false";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, articleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private void softDeleteArticle(Connection conn, String id) throws SQLException {
        String sql = "UPDATE articles SET is_deleted = true, deleted_at = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(2, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Soft deleting article failed, no rows affected.");
            }
        }
    }

    private void softDeleteArticleAndReplies(Connection conn, String id) throws SQLException {
        softDeleteArticle(conn, id);
        String sql = "UPDATE replies SET is_deleted = true, deleted_at = ? WHERE article_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(2, id);
            pstmt.executeUpdate();
        }
    }

    private boolean areAllRepliesByAuthor(Connection conn, String articleId, String userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM replies WHERE article_id = ? AND user_id != ? AND is_deleted = false";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, articleId);
            pstmt.setString(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return true;
    }
}
