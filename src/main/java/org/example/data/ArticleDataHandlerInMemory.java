package org.example.data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.example.domain.Article;

public class ArticleDataHandlerInMemory implements ArticleDataHandler {
    private Map<Long, Article> db = new ConcurrentHashMap<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Article insert(Article article) {
        article = new Article(idGenerator.getAndIncrement(), article.getTitle(),
                article.getContent(),
                article.getAuthor(), article.getCreatedDt(), article.getUserId());
        db.put(article.getArticleId(), article);
        return article;
    }

    @Override
    public Article update(Article article) {
        db.put(article.getArticleId(), article);
        return article;
    }

    @Override
    public Article findByArticleId(Long articleId) {
        return db.get(articleId);
    }

    @Override
    public List<Article> findAll() {
        return db.values().stream().toList();
    }
}