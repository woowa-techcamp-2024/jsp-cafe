package codesquad.global.dao;

import codesquad.article.domain.vo.Status;
import codesquad.common.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ArticleQuery {
    Optional<ArticleResponse> findById(Long id);

    List<ArticleResponse> findAll(QueryRequest queryRequest);

    class QueryRequest extends PageRequest {
        private Status status;

        public QueryRequest(Integer pageNumber, Integer pageSize) {
            super(pageNumber, pageSize);
        }

        public QueryRequest(Integer pageNumber, Integer pageSize, Status status) {
            super(pageNumber, pageSize);
            this.status = status;
        }

        public Status getStatus() {
            return status;
        }
    }

    record ArticleResponse(
            Long articleId,
            String title,
            String content,
            Long writerId,
            String writer
    ) {
    }
}
