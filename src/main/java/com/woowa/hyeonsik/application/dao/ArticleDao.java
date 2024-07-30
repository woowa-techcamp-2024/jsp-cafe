package com.woowa.hyeonsik.application.dao;

import com.woowa.hyeonsik.application.domain.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleDao {
    void save(Article article);
    Optional<Article> findByArticleId(long articleId);
    List<Article> findAll();
    void update(Article article);
}
