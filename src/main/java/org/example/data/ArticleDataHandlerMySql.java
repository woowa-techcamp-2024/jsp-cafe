package org.example.data;

import org.example.domain.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleDataHandlerMySql implements ArticleDataHandler{
    @Override
    public void save(Article article) {
        if (article.getArticleId() == null) {
            insert(article);
        } else {
            update(article);
        }
    }

    private void insert(Article article) {
        String sql = "INSERT INTO articles (title, content, author, created_dt) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setString(3, article.getAuthor());
            pstmt.setTimestamp(4, Timestamp.valueOf(article.getCreatedDt()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert article", e);
        }
    }

    private void update(Article article) {
        String sql = "UPDATE articles SET title = ?, content = ?, author = ?, created_dt = ? WHERE article_id = ?";
        try (Connection con = DatabaseConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setString(3, article.getAuthor());
            pstmt.setTimestamp(4, Timestamp.valueOf(article.getCreatedDt()));
            pstmt.setLong(5, article.getArticleId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update article", e);
        }
    }

    @Override
    public Article findByArticleId(Long articleId) {
        String sql = "SELECT * FROM articles WHERE article_id = ?";
        try (Connection con = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setLong(1, articleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Article(
                            rs.getLong("article_id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getString("author"),
                            rs.getTimestamp("created_dt").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find article by ID", e);
        }
        return null;
    }

    @Override
    public List<Article> findAll() {
        String sql = "SELECT * FROM articles";
        List<Article> articles = new ArrayList<>();
        try (Connection con = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                articles.add(new Article(
                        rs.getLong("article_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("author"),
                        rs.getTimestamp("created_dt").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all articles", e);
        }
        return articles;
    }
}
