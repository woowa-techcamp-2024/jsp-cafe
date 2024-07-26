package org.example.repository;

import org.example.entity.Article;
import org.example.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArticleRepositoryDBImpl implements ArticleRepository {
    private static ArticleRepository instance;


    private ArticleRepositoryDBImpl() {
        // Private constructor to prevent instantiation
    }

    public static ArticleRepository getInstance() {
        if (instance == null) {
            instance = new ArticleRepositoryDBImpl();
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
            e.printStackTrace();
        }
        return article;
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles";
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
            e.printStackTrace();
        }
        return articles;
    }

    @Override
    public Optional<Article> findById(int id) {
        String sql = "SELECT * FROM articles WHERE article_id = ?";
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
            e.printStackTrace();
        }
        return Optional.ofNullable(article);
    }
}