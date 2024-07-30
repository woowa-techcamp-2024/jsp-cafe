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
        String sql = "INSERT INTO articles (id, title, nickname, content, create_at, update_at, is_deleted, deleted_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, article.id());
            pstmt.setString(2, article.title());
            pstmt.setString(3, article.nickname());
            pstmt.setString(4, article.content());
            pstmt.setTimestamp(5, Timestamp.valueOf(article.createAt()));
            pstmt.setTimestamp(6, Timestamp.valueOf(article.updateAt()));
            pstmt.setBoolean(7, false);
            pstmt.setNull(8, Types.TIMESTAMP);

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
        String sql = "UPDATE articles SET title = ?, nickname = ?, content = ?, update_at = ? WHERE id = ? AND is_deleted = false";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updateArticle.title());
            pstmt.setString(2, updateArticle.nickname());
            pstmt.setString(3, updateArticle.content());
            pstmt.setTimestamp(4, Timestamp.valueOf(updateArticle.updateAt()));
            pstmt.setString(5, updateArticle.id());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating article failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating article", e);
        }
    }

    public void delete(final String id) {
        String sql = "UPDATE articles SET is_deleted = true, deleted_at = ? WHERE id = ?";
        try(Connection conn = databaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(2, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Soft deleting article failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error soft deleting article", e);
        }
    }
}
