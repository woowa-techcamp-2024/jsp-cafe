package codesquad.article.infra;

import codesquad.article.handler.dao.ArticleQuery;
import codesquad.article.handler.dto.request.ArticleQueryRequest;
import codesquad.article.handler.dto.response.ArticleDetailResponse;
import codesquad.article.handler.dto.response.ArticleResponse;
import codesquad.comment.domain.vo.Status;
import codesquad.comment.handler.dto.response.CommentResponse;
import codesquad.common.db.connection.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySqlArticleQuery implements ArticleQuery {
    private ConnectionManager connectionManager;

    public MySqlArticleQuery(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Optional<ArticleResponse> findById(Long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = "SELECT a.id AS articleId, a.title, a.content, u.id AS writerId, u.user_id AS writer " +
                    "FROM articles a " +
                    "LEFT JOIN users u ON a.writer = u.user_id " +
                    "WHERE a.id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long articleId = resultSet.getLong("articleId");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                Long writerId = resultSet.getLong("writerId");
                String writer = resultSet.getString("writer");
                return Optional.of(new ArticleResponse(articleId, title, content, writerId, writer));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public Optional<ArticleDetailResponse> findDetailById(Long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = """
                    SELECT a.id AS articleId, a.title, a.content, u.id AS writerId, u.user_id AS writer,
                           c.id AS commentId, c.content AS commentContent, c.writer AS commenter, c.status AS commentStatus,
                           u2.id AS commenterId
                    FROM articles a
                    LEFT JOIN users u ON a.writer = u.user_id
                    LEFT JOIN comments c ON a.id = c.article_id
                    LEFT JOIN users u2 ON c.writer = u2.user_id
                    WHERE a.id = ?
                    """;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            ArticleResponse article = null;
            List<CommentResponse> comments = new ArrayList<>();

            while (resultSet.next()) {
                // Article
                if (article == null) {
                    Long articleId = resultSet.getLong("articleId");
                    String title = resultSet.getString("title");
                    String content = resultSet.getString("content");
                    Long writerId = resultSet.getLong("writerId");
                    String writer = resultSet.getString("writer");
                    article = new ArticleResponse(articleId, title, content, writerId, writer);
                }
                // Comment
                Long commentId = resultSet.getLong("commentId");
                Status commentStatus = Status.of(resultSet.getString("commentStatus"));
                // Check if there are any comments
                if (commentId != 0 && (commentStatus == Status.COMMENTED)) {
                    Long commenterId = resultSet.getLong("commenterId");
                    String commenter = resultSet.getString("commenter");
                    String commentContent = resultSet.getString("commentContent");
                    comments.add(new CommentResponse(commentId, commenterId, commenter, commentContent));
                }
            }
            if (article != null) {
                return Optional.of(new ArticleDetailResponse(article, comments));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public List<ArticleResponse> findAll(ArticleQueryRequest queryRequest) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = """
                    SELECT a.id AS articleId, a.title, a.content, u.id AS writerId, u.user_id AS writer
                    FROM articles a
                    LEFT JOIN users u ON a.writer = u.user_id
                    WHERE a.status = ?
                    ORDER BY a.created_at DESC
                    LIMIT ? OFFSET ?
                    """;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, queryRequest.getStatus().name());
            preparedStatement.setInt(2, queryRequest.getPageSize());
            preparedStatement.setInt(3, queryRequest.getOffset());
            resultSet = preparedStatement.executeQuery();
            List<ArticleResponse> articles = new ArrayList<>();
            while (resultSet.next()) {
                Long articleId = resultSet.getLong("articleId");
                String title = resultSet.getString("title");
                String content = resultSet.getString("content");
                Long writerId = resultSet.getLong("writerId");
                String writer = resultSet.getString("writer");
                if (writer == null) {
                    writer = "알수없는 사용자";
                }
                articles.add(new ArticleResponse(articleId, title, content, writerId, writer));
            }
            return articles;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public Long count(ArticleQueryRequest articleQueryRequest) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = """
                    SELECT COUNT(*) AS count FROM articles a WHERE a.status = ?
                    """;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, articleQueryRequest.getStatus().name());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("count");
            }
            throw new RuntimeException();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }
}
