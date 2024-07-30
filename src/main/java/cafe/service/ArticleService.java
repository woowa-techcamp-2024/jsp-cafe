package cafe.service;

import cafe.domain.db.ArticleDatabase;
import cafe.domain.entity.Article;

import java.util.Map;
import java.util.UUID;

public class ArticleService {
    private final ArticleDatabase articleDatabase;

    public ArticleService(ArticleDatabase articleDatabase) { this.articleDatabase = articleDatabase; }

    public void save(String writer, String title, String contents) {
        save(UUID.randomUUID().toString(), writer, title, contents);
    }

    public void save(String id, String writer, String title, String contents) {
        articleDatabase.insert(Article.of(id, writer, title, contents));
    }

    public Article find(String uri) {
        String id = uri.substring(uri.lastIndexOf("/") + 1);
        Article article = articleDatabase.selectById(id);
        if (article == null) throw new IllegalArgumentException("Question not found!");
        return article;
    }

    public Map<String, Article> findAll() {
        return articleDatabase.selectAll();
    }
}
