package com.woowa.hyeonsik.dao;

import com.woowa.hyeonsik.domain.Article;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ArticleDao {
    private static final Map<Long, Article> articles = new ConcurrentHashMap<>();
    private static final AtomicLong autoIncrement = new AtomicLong(1);

    public void save(Article article) {
        long id = autoIncrement.getAndIncrement();
        articles.put(id, article);
    }

    public Optional<Article> findByArticleId(long articleId) {
        return Optional.ofNullable(articles.get(articleId));
    }

    public List<Article> findAll() {
        return articles.entrySet()
                .stream()
                .map(entry -> entry.getValue())
                .toList();
    }

    public void removeByArticleId(long articleId) {
        articles.remove(articleId);
    }

    public boolean existByArticleId(long articleId) {
        return articles.get(articleId) != null;
    }

    public void clear() {
        articles.clear();
        autoIncrement.set(1);
    }
}
