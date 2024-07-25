package cafe.questions;

import java.util.List;

public interface ArticleRepository {
    Article save(Article article);

    List<Article> findAll();

    Article findById(Long id);
}
