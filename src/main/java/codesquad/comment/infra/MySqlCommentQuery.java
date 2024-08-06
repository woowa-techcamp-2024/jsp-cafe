package codesquad.comment.infra;

import codesquad.comment.handler.dao.CommentQuery;
import codesquad.comment.handler.dto.request.CommentQueryRequest;
import codesquad.comment.handler.dto.response.CommentResponse;
import codesquad.common.db.connection.ConnectionManager;

import java.util.List;

public class MySqlCommentQuery implements CommentQuery {
    private ConnectionManager connectionManager;

    public MySqlCommentQuery(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public List<CommentResponse> findAllByArticleId(CommentQueryRequest query) {
        return List.of();
    }

    @Override
    public Long count(CommentQueryRequest commentQueryRequest) {
        return 0L;
    }
}
