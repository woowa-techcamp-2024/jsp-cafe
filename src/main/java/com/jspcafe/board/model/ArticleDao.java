package com.jspcafe.board.model;

import java.util.*;

public class ArticleDao {
    private final Map<String, Article> articles = new HashMap<>();

    public void save(final Article article) {
        articles.put(article.id(), article);
    }

    public Optional<Article> findById(final String id) {
        return Optional.ofNullable(articles.get(id));
    }

    public List<Article> findAll() {
        return new ArrayList<>(articles.values());
    }
}
