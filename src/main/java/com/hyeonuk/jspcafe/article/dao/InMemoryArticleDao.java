package com.hyeonuk.jspcafe.article.dao;

import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.global.exception.DataIntegrityViolationException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryArticleDao implements ArticleDao{
    private final Map<Long, Article> store = new ConcurrentHashMap<>();
    private final static AtomicLong idGenerator = new AtomicLong(1l);

    @Override
    public Article save(Article article) {
        if(!article.validation()){
            throw new DataIntegrityViolationException("can't persist null or empty value");
        }

        if(article.getId() == null){
            article.setId(idGenerator.incrementAndGet());
        }

        store.put(article.getId(),article);

        return article;
    }

    @Override
    public List<Article> findAll() {
        return store.values()
                .stream()
                .filter(article->article.getDeletedAt() == null)
                .toList();
    }

    @Override
    public Optional<Article> findById(Long id) {
        return store.values()
                .stream()
                .filter(article->article.getId().equals(id) && article.getDeletedAt() == null)
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        store.get(id)
                .setDeletedAt(new Date());
    }
}
