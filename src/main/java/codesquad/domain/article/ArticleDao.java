package codesquad.domain.article;

import java.util.List;
import java.util.Optional;

public interface ArticleDao {
    Long save(Article article);

    Optional<Article> findById(Long id);

    Optional<Article> findByIdForUpdate(Long id);

    List<Article> findAll();

    void update(Article article);
}