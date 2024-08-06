package codesquad.comment.infra;

import codesquad.comment.handler.dao.CommentQuery;
import codesquad.comment.handler.dto.request.CommentQueryRequest;
import codesquad.comment.handler.dto.response.CommentResponse;
import codesquad.common.db.connection.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlCommentQuery implements CommentQuery {
    private ConnectionManager connectionManager;

    public MySqlCommentQuery(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public List<CommentResponse> findAllByArticleId(CommentQueryRequest query) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = """
                    SELECT c.id AS commentId, c.writer, c.content, c.status, u.id AS writerId
                    FROM comments c
                    LEFT JOIN users u ON c.writer = u.user_id
                    WHERE c.article_id = ? AND c.status = ?
                    ORDER BY c.created_at DESC
                    LIMIT ? OFFSET ?
                    """;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, query.getArticleId());
            preparedStatement.setString(2, query.getStatus().name());
            preparedStatement.setInt(3, query.getPageSize());
            preparedStatement.setInt(4, query.getOffset());
            resultSet = preparedStatement.executeQuery();
            List<CommentResponse> comments = new ArrayList<>();
            while (resultSet.next()) {
                Long commentId = resultSet.getLong("commentId");
                String writer = resultSet.getString("writer");
                Long writerId = resultSet.getLong("writerId");
                String content = resultSet.getString("content");
                if (writer == null) {
                    writer = "알수없는 사용자";
                }
                comments.add(new CommentResponse(commentId, writerId, writer, content));
            }
            return comments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connectionManager.close(connection, preparedStatement, resultSet);
        }
    }

    @Override
    public Long count(CommentQueryRequest commentQueryRequest) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            String sql = """
                    SELECT COUNT(*) AS count FROM comments c WHERE c.article_id = ?
                    """;
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, commentQueryRequest.getArticleId());
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
