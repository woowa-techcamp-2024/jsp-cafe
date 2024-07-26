package com.woowa.cafe.repository.qna;

import com.woowa.cafe.domain.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcArticleRepository implements ArticleRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcArticleRepository.class);
    private final DataSource dataSource;

    public JdbcArticleRepository(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Long save(final Article article) {
        try (Connection connection = this.dataSource.getConnection()) {
            String sql = "INSERT INTO articles (writer_id, title, contents, create_at, modified_at) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, article.getWriterId());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getContents());
            pstmt.setTimestamp(4, Timestamp.valueOf(article.getCreatedAt()));
            pstmt.setTimestamp(5, Timestamp.valueOf(article.getUpdatedAt()));

            pstmt.executeUpdate();

            ResultSet resultSet = pstmt.getGeneratedKeys();

            if (resultSet.next()) {
                article.setId(resultSet.getLong(1));
                return article.getId();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public Optional<Article> findById(final Long articleId) {
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM articles WHERE article_id = ?");
            pstmt.setLong(1, articleId);

            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                return Optional.of(new Article(resultSet.getLong("article_id"),
                        resultSet.getString("writer_id"),
                        resultSet.getString("title"),
                        resultSet.getString("contents"),
                        resultSet.getTimestamp("create_at").toLocalDateTime(),
                        resultSet.getTimestamp("modified_at").toLocalDateTime()));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM articles");

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Article article = new Article(resultSet.getLong("article_id"),
                        resultSet.getString("writer_id"),
                        resultSet.getString("title"),
                        resultSet.getString("contents"),
                        resultSet.getTimestamp("create_at").toLocalDateTime(),
                        resultSet.getTimestamp("modified_at").toLocalDateTime());
                articles.add(article);
            }

            return articles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Article> update(final Article article) {
        try (Connection connection = this.dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement("UPDATE articles SET writer_id = ?, title = ?, contents = ?, modified_at = ? WHERE article_id = ?");
            pstmt.setString(1, article.getWriterId());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getContents());
            pstmt.setTimestamp(4, Timestamp.valueOf(article.getUpdatedAt()));
            pstmt.setLong(5, article.getId());

            pstmt.executeUpdate();

            return Optional.of(article);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(final Long articleId) {

    }
}
