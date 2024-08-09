package cafe.questions.repository;

import cafe.questions.Article;

import java.util.List;

public interface ArticleRepository {
    Article save(Article article);

    List<Article> findAll();

    Article findById(Long id);

    void deleteAll();

    void deleteById(Long id);

    List<Article> findAllPaginated(int page);

    long count();
}
