package codesquad.jspcafe.domain.article.repository;

import codesquad.jspcafe.common.database.JDBCConnectionManager;
import codesquad.jspcafe.domain.article.domain.Article;
import codesquad.jspcafe.domain.user.domain.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArticleJdbcRepository implements ArticleRepository {

    private static final Logger log = LoggerFactory.getLogger(ArticleJdbcRepository.class);
    private static final String SCHEMA_DDL = """
        CREATE TABLE IF NOT EXISTS articles
        (
            id        BIGINT AUTO_INCREMENT PRIMARY KEY,
            title     VARCHAR(255) NOT NULL,
            writer    BIGINT NOT NULL,
            contents  TEXT         NOT NULL,
            createdAt DATETIME,
            deletedAt DATETIME
        );""";

    private final JDBCConnectionManager connectionManager;

    public ArticleJdbcRepository(JDBCConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        init();
    }

    @Override
    public Article save(Article article) {
        String insertQuery = "INSERT INTO articles (title, writer, contents, createdAt) VALUES ( ?, ?, ?, ? )";
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, article.getTitle());
            preparedStatement.setLong(2, article.getWriter().getId());
            preparedStatement.setString(3, article.getContents());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(article.getCreatedAt()));
            preparedStatement.executeUpdate();
            ResultSet generatedKey = preparedStatement.getGeneratedKeys();
            if (generatedKey.next()) {
                Long generatedId = generatedKey.getLong(1);
                article.setId(generatedId);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return article;
    }

    @Override
    public Article update(Article article) {
        String updateQuery = "UPDATE articles SET title = ?, contents = ? WHERE id = ? AND deletedAt IS NULL";
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, article.getTitle());
            preparedStatement.setString(2, article.getContents());
            preparedStatement.setLong(3, article.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return article;
    }

    @Override
    public Long delete(Article article) {
        String deleteQuery = "UPDATE articles SET deletedAt = NOW() WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setLong(1, article.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return article.getId();
    }

    @Override
    public Optional<Article> findById(Long id) {
        String findByIdQuery = "SELECT a.id, a.title, u.id, u.user_id, u.password, u.username, u.email, a.contents, a.createdAt FROM articles a, users u WHERE a.id = ? AND a.writer = u.id AND a.deletedAt IS NULL";
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(findByIdQuery)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Article article = getArticle(resultSet);
                return Optional.of(article);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public long count() {
        String findIdQuery = "SELECT count(id) FROM articles WHERE deletedAt IS NULL";
        try (Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(findIdQuery)) {
            while (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return 0L;
    }

    @Override
    public List<Article> findByPage(int page, int limit) {
        long id = -1;
        List<Article> articles = new ArrayList<>();
        String findIdQuery = "SELECT id from articles WHERE deletedAt IS NULL ORDER BY id DESC LIMIT 1 OFFSET ?";
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(findIdQuery)) {
            preparedStatement.setInt(1, (page - 1) * limit);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getLong(1) + 1L;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        String findByIdQuery = "SELECT a.id, a.title, u.id, u.user_id, u.password, u.username, u.email, a.contents, a.createdAt FROM articles a, users u WHERE a.writer = u.id AND a.id < ? AND a.deletedAt IS NULL ORDER BY a.id DESC LIMIT ?";
        try (Connection connection = connectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(findByIdQuery)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setInt(2, limit + 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Article article = getArticle(resultSet);
                articles.add(article);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Collections.unmodifiableList(articles);
    }

    private void init() {
        try (Connection connection = connectionManager.getConnection();
            Statement statement = connection.createStatement()) {
            statement.execute(SCHEMA_DDL);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    private Article getArticle(ResultSet resultSet) throws SQLException {
        return new Article(
            resultSet.getLong("a.id"),
            resultSet.getString("a.title"),
            new User(
                resultSet.getLong("u.id"),
                resultSet.getString("u.user_id"),
                resultSet.getString("u.password"),
                resultSet.getString("u.username"),
                resultSet.getString("u.email")
            ),
            resultSet.getString("a.contents"),
            resultSet.getTimestamp("a.createdAt").toLocalDateTime()
        );
    }
}
