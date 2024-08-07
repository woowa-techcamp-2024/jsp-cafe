package com.hyeonuk.jspcafe.article.dao;

import com.hyeonuk.jspcafe.article.domain.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleDao {
    Article save(Article article);
    List<Article> findAll();
    List<Article> findAll(int size,int page);
    Optional<Article> findById(Long id);
    void deleteById(Long id);
    long count();
}
