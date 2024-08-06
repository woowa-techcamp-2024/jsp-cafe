package codesquad.comment.service;

import codesquad.comment.handler.dao.CommentQuery;
import codesquad.comment.handler.dto.request.CommentQueryRequest;
import codesquad.comment.handler.dto.response.CommentResponse;
import codesquad.comment.handler.dto.response.PagedCommentResponse;
import codesquad.common.http.response.PageInfo;
import codesquad.common.utility.PageUtils;

import java.util.List;

public class QueryCommentService {
    private final CommentQuery commentQuery;

    public QueryCommentService(CommentQuery commentQuery) {
        this.commentQuery = commentQuery;
    }

    public PagedCommentResponse<CommentResponse> findByArticleId(CommentQueryRequest query) {
        List<CommentResponse> content = commentQuery.findAllByArticleId(query);
        PageInfo pageInfo = PageUtils.getPageInfo(commentQuery::count, query);
        return new PagedCommentResponse<>(content, pageInfo);
    }
}
