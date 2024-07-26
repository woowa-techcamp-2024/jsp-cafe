package cafe.service;

import cafe.domain.db.ArticleDatabase;
import cafe.domain.entity.Article;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class ArticleService {
    private final ArticleDatabase articleDatabase;

    public ArticleService(ArticleDatabase articleDatabase) { this.articleDatabase = articleDatabase; }

    public void save(String writer, String title, String contents) {
        articleDatabase.insert(Article.of(writer, title, contents));
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
