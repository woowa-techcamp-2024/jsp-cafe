package org.example.data;

import java.util.List;
import org.example.domain.Article;

public interface ArticleDataHandler {
    Article insert(Article article);

    Article update(Article article);

    Article findByArticleId(Long articleId);

    List<Article> findAll();

    List<Article> findByPage(int pageNumber);

    int getTotalPageNumber();
}
