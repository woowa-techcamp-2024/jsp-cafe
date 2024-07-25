package org.example.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.example.entity.Article;

public class ArticleRepositoryMemoryImpl implements ArticleRepository {
    private static ArticleRepositoryMemoryImpl instance;

    private final Map<Integer, Article> articles = new ConcurrentHashMap<>();
    private final AtomicInteger index = new AtomicInteger(0);

    private ArticleRepositoryMemoryImpl() {
        // Private constructor to prevent instantiation
    }

    public static ArticleRepositoryMemoryImpl getInstance() {
        if (instance == null) {
            instance = new ArticleRepositoryMemoryImpl();
        }
        return instance;
    }

    @Override
    public Article save(Article article) {
        int id = index.incrementAndGet();
        article.setId(id);
        articles.put(id, article);
        return article;
    }

    @Override
    public List<Article> findAll() {
        return new ArrayList<>(articles.values());
    }

    @Override
    public Optional<Article> findById(int id) {
        return Optional.ofNullable(articles.get(id));
    }
}