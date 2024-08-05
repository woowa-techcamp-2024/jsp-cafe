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
        String sql = "INSERT INTO articles (writer_id, title, contents, reply_count, is_deleted, created_at, modified_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, article.getWriterId());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getContents());
            pstmt.setLong(4, article.getReplyCount());
            pstmt.setBoolean(5, false);
            pstmt.setTimestamp(6, Timestamp.valueOf(article.getCreatedAt()));
            pstmt.setTimestamp(7, Timestamp.valueOf(article.getUpdatedAt()));

            pstmt.executeUpdate();

            try (ResultSet resultSet = pstmt.getGeneratedKeys()) {
                if (resultSet.next()) {
                    article.setId(resultSet.getLong(1));
                    return article.getId();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Optional<Article> findById(final Long articleId) {
        String sql = "SELECT * FROM articles WHERE article_id = ? AND is_deleted = false";
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleId);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Article(resultSet.getLong("article_id"),
                            resultSet.getString("writer_id"),
                            resultSet.getString("title"),
                            resultSet.getString("contents"),
                            resultSet.getLong("reply_count"),
                            resultSet.getTimestamp("created_at").toLocalDateTime(),
                            resultSet.getTimestamp("modified_at").toLocalDateTime()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM articles WHERE is_deleted = false";
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet resultSet = pstmt.executeQuery()) {
            while (resultSet.next()) {
                Article article = new Article(resultSet.getLong("article_id"),
                        resultSet.getString("writer_id"),
                        resultSet.getString("title"),
                        resultSet.getString("contents"),
                        resultSet.getLong("reply_count"),
                        resultSet.getTimestamp("created_at").toLocalDateTime(),
                        resultSet.getTimestamp("modified_at").toLocalDateTime());
                articles.add(article);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return articles;
    }

    @Override
    public Optional<Article> update(final Article article) {
        String sql = "UPDATE articles SET writer_id = ?, title = ?, contents = ?, reply_count = ?, modified_at = ? WHERE article_id = ? AND is_deleted = false";
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, article.getWriterId());
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getContents());
            pstmt.setLong(4, article.getReplyCount());
            pstmt.setTimestamp(5, Timestamp.valueOf(article.getUpdatedAt()));
            pstmt.setLong(6, article.getId());

            pstmt.executeUpdate();
            return Optional.of(article);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(final Long articleId) {
        String sql = "UPDATE articles SET is_deleted = true WHERE article_id = ?";
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, articleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
