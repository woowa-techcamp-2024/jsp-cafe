package codesquad.comment.handler.dao;

import codesquad.comment.handler.dto.request.CommentQueryRequest;
import codesquad.comment.handler.dto.response.CommentResponse;

import java.util.List;

public interface CommentQuery {
    List<CommentResponse> findAllByArticleId(CommentQueryRequest query);

    Long count(CommentQueryRequest commentQueryRequest);
}
