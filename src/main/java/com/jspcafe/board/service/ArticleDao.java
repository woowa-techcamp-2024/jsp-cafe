package com.jspcafe.board.service;

import com.jspcafe.board.model.Article;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArticleDao {
    private final Map<String, Article> articles = new HashMap<>();

    public void save(final Article article) {
        articles.put(article.id(), article);
    }

    public Optional<Article> findById(final String id) {
        return Optional.ofNullable(articles.get(id));
    }
}
