package com.woowa.hyeonsik.application.dao;

import com.woowa.hyeonsik.application.domain.Article;
import com.woowa.hyeonsik.application.domain.Page;

import java.util.Optional;

public interface ArticleDao {
    void save(Article article);
    Optional<Article> findByArticleId(long articleId);
    Page<Article> findAll(long page);
    void update(Article article);
    void removeByArticleId(long articleId);
}
