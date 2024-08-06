package codesquad.comment.handler.dto.request;

import codesquad.comment.domain.vo.Status;
import codesquad.common.http.request.PageRequest;

public class CommentQueryRequest extends PageRequest {
    private Long articleId;
    private Status status;

    public CommentQueryRequest(Integer pageNumber, Integer pageSize, Long articleId, Status status) {
        super(pageNumber, pageSize);
        this.articleId = articleId;
        this.status = status;
    }

    public Long getArticleId() {
        return articleId;
    }

    public Status getStatus() {
        return status;
    }
}
