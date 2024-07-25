package woowa.camp.jspcafe.repository;

import java.util.List;
import java.util.Optional;
import woowa.camp.jspcafe.domain.Article;

public interface ArticleRepository {

    Long save(Article article);

    Optional<Article> findById(Long id);

    Optional<Article> findPrevious(Long currentId);

    Optional<Article> findNext(Long currentId);

    List<Article> findByOffsetPagination(int offset, int limit);

}
