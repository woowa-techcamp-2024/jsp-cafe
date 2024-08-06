package codesquad.article.handler.dto.request;

import codesquad.article.domain.vo.Status;
import codesquad.common.http.request.PageRequest;

public class ArticleQueryRequest extends PageRequest {
    private Status status;

    public ArticleQueryRequest(Integer pageNumber, Integer pageSize) {
        super(pageNumber, pageSize);
    }

    public ArticleQueryRequest(Integer pageNumber, Integer pageSize, Status status) {
        super(pageNumber, pageSize);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
