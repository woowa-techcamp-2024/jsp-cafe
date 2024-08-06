package org.example.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.example.constance.AliveStatus;
import org.example.domain.Article;

public class ArticleDataHandlerMySql implements ArticleDataHandler {
    private final int PAGE_SIZE = 15;

    public Article insert(Article article) {
        String sql = "INSERT INTO articles (title, content, author, created_dt, alive_status, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setString(3, article.getAuthor());
            pstmt.setTimestamp(4, Timestamp.valueOf(article.getCreatedDt()));
            pstmt.setString(5, article.getAlivestatus().name());
            pstmt.setLong(6, article.getUserId());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    article = new Article(id, article.getTitle(), article.getContent(), article.getAuthor(),
                            article.getCreatedDt(), article.getAlivestatus(), article.getUserId());
                } else {
                    throw new SQLException("Creating article failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert article", e);
        }
        return article;
    }

    public Article update(Article article) {
        String sql = "UPDATE articles SET title = ?, content = ?, author = ?, created_dt = ?, alive_status = ?, user_id = ? WHERE article_id = ?";
        try (Connection con = DatabaseConnectionManager.getConnection(); PreparedStatement pstmt = con.prepareStatement(
                sql)) {
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setString(3, article.getAuthor());
            pstmt.setTimestamp(4, Timestamp.valueOf(article.getCreatedDt()));
            pstmt.setString(5, article.getAlivestatus().name());
            pstmt.setLong(6, article.getUserId());
            pstmt.setLong(7, article.getArticleId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update article", e);
        }
        return article;
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
                            rs.getTimestamp("created_dt").toLocalDateTime(),
                            AliveStatus.valueOf(rs.getString("alive_status")),
                            rs.getLong("user_id")
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
        String sql = "SELECT * FROM articles where articles.alive_status = ?";
        List<Article> articles = new ArrayList<>();
        try (Connection con = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setString(1, AliveStatus.ALIVE.name());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                articles.add(new Article(
                        rs.getLong("article_id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("author"),
                        rs.getTimestamp("created_dt").toLocalDateTime(),
                        AliveStatus.valueOf(rs.getString("alive_status")),
                        rs.getLong("user_id")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find all articles", e);
        }
        return articles;
    }

    @Override
    public List<Article> findByPage(int pageNumber) {
        String sql = "SELECT * FROM articles WHERE alive_status = ? ORDER BY created_dt DESC, title DESC LIMIT ? OFFSET ?";
        List<Article> articles = new ArrayList<>();
        try (Connection con = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, AliveStatus.ALIVE.name());
            pstmt.setInt(2, PAGE_SIZE);
            pstmt.setInt(3, (pageNumber - 1) * PAGE_SIZE);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    articles.add(new Article(
                            rs.getLong("article_id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getString("author"),
                            rs.getTimestamp("created_dt").toLocalDateTime(),
                            AliveStatus.valueOf(rs.getString("alive_status")),
                            rs.getLong("user_id")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find articles by page", e);
        }
        return articles;
    }


    public int getTotalPageNumber() {
        String sql = "SELECT COUNT(*) as total FROM articles WHERE alive_status = ?";
        try (Connection con = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, AliveStatus.ALIVE.name());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int totalArticles = rs.getInt("total");
                    return (int) Math.ceil((double) totalArticles / PAGE_SIZE);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get total pages", e);
        }
        return 0;
    }
}
