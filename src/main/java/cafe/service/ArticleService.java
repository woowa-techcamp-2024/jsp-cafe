package cafe.service;

import cafe.domain.db.ArticleDatabase;
import cafe.domain.entity.Article;
import cafe.domain.entity.User;

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
        String id = uri.split("/")[2];
        Article article = articleDatabase.selectById(id);
        if (article == null) throw new IllegalArgumentException("Question not found!");
        return article;
    }

    public Map<String, Article> findAll() {
        return articleDatabase.selectAll();
    }

    public void verifyArticleId(User user, String requestURI) {
        String id = requestURI.split("/")[2];
        Article article = articleDatabase.selectById(id);
        if (article == null) throw new IllegalArgumentException("게시글이 없습니다!");
        if (!article.getWriter().equals(user.getUserId())) throw new IllegalArgumentException("다른 사람의 글을 수정할 수 없습니다!");
    }

    public void deleteById(String requestURI) {
        String id = requestURI.split("/")[2];
        articleDatabase.deleteById(id);
    }

    public void update(String requestURI, String title, String contents) {
        String id = requestURI.split("/")[2];
        Article article = articleDatabase.selectById(id);
        articleDatabase.update(id, Article.of(id, article.getWriter(), title, contents));
    }
}
