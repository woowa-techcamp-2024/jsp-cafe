package org.example.data;

import org.example.domain.Article;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class ArticleDataHandlerInMemory implements ArticleDataHandler{
    private Map<Long, Article> db = new ConcurrentHashMap<>();
    private AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public void save(Article article) {
        if(article.getArticleId() == null){
            article = new Article(idGenerator.getAndIncrement(), article.getTitle(), article.getContent(), article.getAuthor(), article.getCreatedDt());
        }
        db.put(article.getArticleId(), article);
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
