package woowa.camp.jspcafe.repository.article;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import woowa.camp.jspcafe.domain.Article;

public interface ArticleRepository {

    Long save(Article article);

    Optional<Article> findById(Long id);

    Optional<Article> findPrevious(Long currentId);

    Optional<Article> findNext(Long currentId);

    List<Article> findByOffsetPagination(int offset, int limit);

    void update(Article article);

    void softDeleteById(Long id, LocalDate deletedTime);

    Long findAllArticleCounts();

}
