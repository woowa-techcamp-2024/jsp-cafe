package org.example.repository;

import org.example.entity.Article;
import org.example.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.example.util.LoggerUtil;
import org.slf4j.Logger;

public class ArticleRepositoryDBImpl implements ArticleRepository {
    private static ArticleRepository instance;
    private final Logger logger = LoggerUtil.getLogger();


    private ArticleRepositoryDBImpl() {
        // Private constructor to prevent instantiation
    }

    public static ArticleRepository getInstance() {
        if (instance == null) {
            instance = new ArticleRepositoryDBImpl();
            instance.save(new Article("title1", "content1", "test"));
            instance.save(new Article("title2", "content2", "test2"));
        }
        return instance;
    }

    @Override
    public Article save(Article article) {
        String sql = "INSERT INTO articles (title, content, author) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setString(3, article.getAuthor());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    article.setArticleId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to save article", e);
        }
        return article;
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles WHERE deleted = FALSE";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Article article = new Article(
                    rs.getInt("article_id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getString("author")
                );
                article.setArticleId(rs.getInt("article_id"));
                articles.add(article);
            }
        } catch (SQLException e) {
            logger.error("Failed to find articles", e);
        }
        return articles;
    }

    @Override
    public Optional<Article> findById(int id) {
        String sql = "SELECT * FROM articles WHERE article_id = ? AND deleted = FALSE";
        Article article = null;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    article = new Article(
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("author")
                    );
                    article.setArticleId(rs.getInt("article_id"));
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to find article", e);
        }
        return Optional.ofNullable(article);
    }

    @Override
    public void update(int id, String title, String content, String userId) {
        String sql = "UPDATE articles SET title = ?, content = ? WHERE article_id = ? AND author = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setInt(3, id);
            pstmt.setString(4, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to update article", e);
        }
    }

    @Override
    public void deleteById(int id) {
        //soft delete
        String sql = "UPDATE articles SET deleted = TRUE WHERE article_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete article", e);
        }
    }
}