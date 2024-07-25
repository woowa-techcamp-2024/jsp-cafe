package org.example.data;

import org.example.domain.Article;

import java.util.List;

public interface ArticleDataHandler {
    Article insert(Article article);
    Article update(Article article);
    Article findByArticleId(Long articleId);
    List<Article> findAll();
}
