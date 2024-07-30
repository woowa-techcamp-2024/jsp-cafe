package cafe.questions.repository;


import cafe.database.ConnectionPool;
import cafe.questions.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcArticleRepository implements ArticleRepository {
    private final ConnectionPool connectionPool;

    public JdbcArticleRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public Article save(Article article) {
        if (article.getId() == null) {
            return insert(article);
        } else {
            return update(article);
        }
    }

    private Article insert(Article article) {
        String sql = "INSERT INTO articles (user_id, title, content) VALUES (?, ?, ?)";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, article.getUserId() == null ? 0 : article.getUserId());
            statement.setString(2, article.getTitle());
            statement.setString(3, article.getContent());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Failed to save article, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return article.withId(generatedKeys.getLong(1));
                } else {
                    throw new RuntimeException("Failed to save article, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Article update(Article article) {
        String sql = "UPDATE articles SET user_id = ?, title = ?, content = ? WHERE id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, article.getUserId());
            statement.setString(2, article.getTitle());
            statement.setString(3, article.getContent());
            statement.setLong(4, article.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Failed to update article, no rows affected.");
            }
            return article;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Article> findAll() {
        String sql = "SELECT * FROM articles ORDER BY id DESC";
        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            List<Article> articles = new ArrayList<>();
            while (resultSet.next()) {
                articles.add(new Article(
                        resultSet.getLong("id"),
                        resultSet.getLong("user_id"),
                        resultSet.getString("title"),
                        resultSet.getString("content"),
                        resultSet.getTimestamp("created_date"),
                        resultSet.getTimestamp("updated_date")
                ));
            }
            return articles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Article findById(Long id) {
        String sql = "SELECT * FROM articles WHERE id = ?";
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Article(
                            resultSet.getLong("id"),
                            resultSet.getLong("user_id"),
                            resultSet.getString("title"),
                            resultSet.getString("content"),
                            resultSet.getTimestamp("created_date"),
                            resultSet.getTimestamp("updated_date")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM articles";
        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}