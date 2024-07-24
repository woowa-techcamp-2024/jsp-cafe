package org.example.data;

import org.example.domain.Article;

import java.util.List;

public interface ArticleDataHandler {
    Article save(Article article);
    Article findByArticleId(Long articleId);
    List<Article> findAll();
}
