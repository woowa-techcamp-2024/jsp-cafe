package repository.article;

import domain.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    void saveArticle(Article article);

    List<Article> findAll();

    Optional<Article> findById(Long id);
}
