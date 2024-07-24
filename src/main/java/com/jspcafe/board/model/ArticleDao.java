package com.jspcafe.board.model;

import com.jspcafe.util.DatabaseConnector;

import java.sql.*;
import java.util.*;

public class ArticleDao {
    private final DatabaseConnector databaseConnector;

    public ArticleDao(final DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public void save(final Article article) {
        String sql = "INSERT INTO articles (id, title, nickname, content, create_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, article.id());
            pstmt.setString(2, article.title());
            pstmt.setString(3, article.nickname());
            pstmt.setString(4, article.content());
            pstmt.setTimestamp(5, Timestamp.valueOf(article.createAt()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving article", e);
        }
    }

    public Optional<Article> findById(final String id) {
        String sql = "SELECT * FROM articles WHERE id = ?";
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
                            rs.getTimestamp("create_at").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding article by id", e);
        }
        return Optional.empty();
    }

    public List<Article> findAll() {
        String sql = "SELECT * FROM articles ORDER BY create_at DESC";
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
                        rs.getTimestamp("create_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all articles", e);
        }
        return articles;
    }
}
