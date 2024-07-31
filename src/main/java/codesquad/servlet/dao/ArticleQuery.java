package codesquad.servlet.dao;

import java.util.List;
import java.util.Optional;

public interface ArticleQuery {
    Optional<ArticleResponse> findById(Long id);

    List<ArticleResponse> findAll();

    record ArticleResponse(
            Long articleId,
            String title,
            String content,
            Long writerId,
            String writer
    ) {
    }
}
