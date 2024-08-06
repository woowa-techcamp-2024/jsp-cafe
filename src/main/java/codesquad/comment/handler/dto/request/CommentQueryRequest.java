package codesquad.comment.handler.dto.request;

import codesquad.common.http.request.PageRequest;

public class CommentQueryRequest extends PageRequest {
    private Long articleId;

    public CommentQueryRequest(Integer pageNumber, Integer pageSize) {
        super(pageNumber, pageSize);
    }

    public CommentQueryRequest(Integer pageNumber, Integer pageSize, Long articleId) {
        super(pageNumber, pageSize);
        this.articleId = articleId;
    }

    public Long getArticleId() {
        return articleId;
    }
}
