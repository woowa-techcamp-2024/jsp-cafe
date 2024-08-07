package codesquad.jspcafe.domain.article.repository;

import codesquad.jspcafe.domain.article.domain.Article;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    Article save(Article article);

    Article update(Article article);

    Long delete(Article article);

    Optional<Article> findById(Long id);

    long count();

    List<Article> findByPage(int page, int limit);

}
