package woowa.camp.jspcafe.repository.article;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import woowa.camp.jspcafe.domain.Article;
import woowa.camp.jspcafe.infra.DatabaseConnector;

public class DBArticleRepository implements ArticleRepository {

    private final DatabaseConnector connector;

    public DBArticleRepository(DatabaseConnector connector) {
        this.connector = connector;
    }

    @Override
    public Long save(Article article) {
        String sql = "INSERT INTO articles (author_id, title, content, hits, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 익명게시판은 작성자 id가 null 이다.
            if (article.getAuthorId() == null) {
                pstmt.setNull(1, Types.BIGINT);
            } else {
                pstmt.setLong(1, article.getAuthorId());
            }
            pstmt.setString(2, article.getTitle());
            pstmt.setString(3, article.getContent());
            pstmt.setInt(4, article.getHits());
            pstmt.setDate(5, Date.valueOf(article.getCreatedAt()));
            pstmt.setDate(6, Date.valueOf(article.getUpdatedAt()));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("게시글 저장을 실패했습니다. 영향을 받은 행이 없습니다.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    article.setId(generatedKeys.getLong(1));
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("게시글 저장을 실패했습니다. id를 획득하지 못했습니다.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Article> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        String sql = "SELECT * FROM articles WHERE id = ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToArticle(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<Article> findPrevious(Long currentId) {
        String sql = "SELECT * FROM articles WHERE id < ? ORDER BY id DESC LIMIT 1";

        try (Connection connection = connector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setLong(1, currentId);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToArticle(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }


    @Override
    public Optional<Article> findNext(Long currentId) {
        String sql = "SELECT * FROM articles WHERE id > ? ORDER BY id ASC LIMIT 1";

        try (Connection connection = connector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setLong(1, currentId);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRowToArticle(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }


    @Override
    public List<Article> findByOffsetPagination(int offset, int limit) {
        String sql = "SELECT * FROM articles ORDER BY created_at DESC, id DESC LIMIT ? OFFSET ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                List<Article> articles = new ArrayList<>();
                while (resultSet.next()) {
                    articles.add(mapRowToArticle(resultSet));
                }
                return articles;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Article article) {
        String sql = "UPDATE articles SET title = ?, content = ?, hits = ?, updated_at = ? WHERE id = ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setInt(3, article.getHits());
            pstmt.setDate(4, Date.valueOf(article.getUpdatedAt()));
            pstmt.setLong(5, article.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Article with id " + article.getId() + " not found");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update article", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM articles WHERE id = ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting article failed, no rows affected.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete article", e);
        }
    }

    private Article mapRowToArticle(ResultSet resultSet) throws SQLException {
        Long authorId = resultSet.getLong("author_id");
        boolean wasNull = resultSet.wasNull();
        authorId = wasNull ? null : authorId; // resultSet.getLong == null 이면, 기본값으로 0을 세팅한다.

        Article article = new Article(
                authorId,
                resultSet.getString("title"),
                resultSet.getString("content"),
                resultSet.getInt("hits"),
                resultSet.getDate("created_at").toLocalDate(),
                resultSet.getDate("updated_at").toLocalDate()
        );
        article.setId(resultSet.getLong("id"));
        return article;
    }

}
