package codesquad.article.repository;

import codesquad.article.domain.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {
    Long save(Article article);

    Optional<Article> findById(Long id);

    Optional<Article> findByIdForUpdate(Long id);

    Optional<Article> findByIdForShare(Long id);

    List<Article> findAll();

    void update(Article article);
}
