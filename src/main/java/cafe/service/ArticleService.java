package cafe.service;

import cafe.domain.db.ArticleDatabase;
import cafe.domain.entity.Article;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public class ArticleService {
    private final ArticleDatabase articleDatabase;

    public ArticleService(ArticleDatabase articleDatabase) { this.articleDatabase = articleDatabase; }

    public void save(HttpServletRequest req, HttpServletResponse resp) {
        String writer = req.getParameter("writer");
        String title = req.getParameter("title");
        String contents = req.getParameter("contents");

        articleDatabase.save(Article.of(writer, title, contents));
    }

    public Article find(HttpServletRequest req, HttpServletResponse resp) {
        String uri = req.getRequestURI();
        String id = uri.substring(uri.lastIndexOf("/") + 1);

        Article article = articleDatabase.find(id);
        if (article == null) throw new IllegalArgumentException("Question not found!");
        return article;
    }

    public Map<String, Article> findAll(HttpServletRequest req, HttpServletResponse resp) {
        return articleDatabase.findAll();
    }
}
