package woowa.camp.jspcafe.repository.article;

import java.util.List;
import java.util.Optional;
import woowa.camp.jspcafe.domain.Article;
import woowa.camp.jspcafe.repository.dto.ArticleUpdateRequest;

public interface ArticleRepository {

    Long save(Article article);

    Optional<Article> findById(Long id);

    Optional<Article> findPrevious(Long currentId);

    Optional<Article> findNext(Long currentId);

    List<Article> findByOffsetPagination(int offset, int limit);

    void update(Long id, ArticleUpdateRequest articleUpdateRequest);

}
