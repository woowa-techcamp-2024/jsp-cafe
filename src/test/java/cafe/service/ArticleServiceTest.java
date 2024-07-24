package cafe.service;

import cafe.domain.db.ArticleDatabase;
import cafe.domain.entity.Article;
import cafe.servlet.TestHttpServletRequest;
import cafe.servlet.TestHttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ArticleServiceTest {
    private ArticleService articleService;
    private ArticleDatabase articleDatabase;
    private TestHttpServletRequest req;
    private TestHttpServletResponse resp;

    @BeforeEach
    public void setUp() {
        articleDatabase = new ArticleDatabase();
        articleService = new ArticleService(articleDatabase);
        req = new TestHttpServletRequest();
        resp = new TestHttpServletResponse();
    }

    @Test
    @DisplayName("Article 객체를 데이터베이스에 저장하는 기능 테스트")
    public void testSave() {
        req.setParameter("writer", "author1");
        req.setParameter("title", "Test Title");
        req.setParameter("contents", "Test Contents");

        articleService.save(req, resp);

        Map<String, Article> allArticles = articleDatabase.findAll();
        assertEquals(1, allArticles.size());
        Article savedArticle = allArticles.values().iterator().next();
        assertEquals("author1", savedArticle.getWriter());
        assertEquals("Test Title", savedArticle.getTitle());
        assertEquals("Test Contents", savedArticle.getContents());
        assertNotNull(savedArticle.getCreated());
    }

    @Test
    @DisplayName("ID로 Article 객체를 조회하는 기능 테스트")
    public void testFind() {
        Article article = Article.of("author1", "Test Title", "Test Contents");
        articleDatabase.save(article);

        String articleId = articleDatabase.findAll().keySet().iterator().next();
        req.setRequestURI("/articles/" + articleId);

        Article foundArticle = articleService.find(req, resp);

        assertNotNull(foundArticle);
        assertEquals("author1", foundArticle.getWriter());
        assertEquals("Test Title", foundArticle.getTitle());
        assertEquals("Test Contents", foundArticle.getContents());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 Article을 조회할 때 예외 발생 여부 테스트")
    public void testFindByIdNotFound() {
        req.setRequestURI("/articles/nonexistent-id");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            articleService.find(req, resp);
        });

        assertEquals("Question not found!", exception.getMessage());
    }

    @Test
    @DisplayName("모든 Article 객체를 조회하는 기능 테스트")
    public void testFindAll() {
        Article article1 = Article.of("author1", "Test Title 1", "Test Contents 1");
        Article article2 = Article.of("author2", "Test Title 2", "Test Contents 2");
        articleDatabase.save(article1);
        articleDatabase.save(article2);

        Map<String, Article> allArticles = articleService.findAll(req, resp);

        assertEquals(2, allArticles.size());
        assertTrue(allArticles.containsValue(article1));
        assertTrue(allArticles.containsValue(article2));
    }
}