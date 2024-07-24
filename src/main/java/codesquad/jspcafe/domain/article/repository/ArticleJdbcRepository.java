package codesquad.jspcafe.domain.article.repository;

import codesquad.jspcafe.common.database.MySQLConnectionManager;
import codesquad.jspcafe.domain.article.domain.Article;
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

    private final MySQLConnectionManager mySQLConnectionManager;

    public ArticleJdbcRepository(MySQLConnectionManager mySQLConnectionManager) {
        this.mySQLConnectionManager = mySQLConnectionManager;
    }

    @Override
    public Article save(Article article) {
        String insertQuery = "INSERT INTO articles (title, writer, contents, createdAt) VALUES ( ?, ?, ?, ? )";
        try (Connection connection = mySQLConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, article.getTitle());
            preparedStatement.setString(2, article.getWriter());
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
    public Optional<Article> findById(Long id) {
        String findByIdQuery = "SELECT id, title, writer, contents, createdAt FROM articles WHERE id = ?";
        try (Connection connection = mySQLConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(findByIdQuery)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Article article = new Article(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getString("writer"),
                    resultSet.getString("contents"),
                    resultSet.getTimestamp("createdAt").toLocalDateTime()
                );
                return Optional.of(article);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        String findAllQuery = "SELECT id, title, writer, contents, createdAt FROM articles";
        try (Connection connection = mySQLConnectionManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(findAllQuery)) {
            while (resultSet.next()) {
                Article article = new Article(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getString("writer"),
                    resultSet.getString("contents"),
                    resultSet.getTimestamp("createdAt").toLocalDateTime()
                );
                articles.add(article);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Collections.unmodifiableList(articles);
    }
}
