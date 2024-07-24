package codesquad.article;

import java.util.List;
import java.util.Optional;

public interface ArticleDao {
    Long save(Article article);

    Optional<Article> findById(Long id);

    List<Article> findAll();
}
