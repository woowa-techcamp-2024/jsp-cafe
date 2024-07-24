package cafe.domain.db;

import cafe.domain.entity.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ArticleDatabaseTest {
    private ArticleDatabase articleDatabase;
    private Article article;

    @BeforeEach
    public void setUp() {
        articleDatabase = new ArticleDatabase();
        article = Article.of("author1", "Test Title", "Test Contents");
    }

    @Test
    @DisplayName("Article 객체가 데이터베이스에 올바르게 저장되고 조회되는지 테스트")
    public void testSaveAndFind() {
        articleDatabase.save(article);
        Map<String, Article> allArticles = articleDatabase.findAll();
        Article foundArticle = allArticles.values().iterator().next();

        assertEquals(1, allArticles.size());
        assertEquals(article.getWriter(), foundArticle.getWriter());
        assertEquals(article.getTitle(), foundArticle.getTitle());
        assertEquals(article.getContents(), foundArticle.getContents());
        assertNotNull(foundArticle.getCreated());
    }

    @Test
    @DisplayName("데이터베이스에서 Article을 ID로 조회하는 기능 테스트")
    public void testFindById() {
        articleDatabase.save(article);
        String articleId = articleDatabase.findAll().keySet().iterator().next();
        Article foundArticle = articleDatabase.find(articleId);

        assertNotNull(foundArticle);
        assertEquals(article.getWriter(), foundArticle.getWriter());
        assertEquals(article.getTitle(), foundArticle.getTitle());
        assertEquals(article.getContents(), foundArticle.getContents());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 Article을 조회할 때 null을 반환하는지 테스트")
    public void testFindByIdNotFound() {
        Article foundArticle = articleDatabase.find(UUID.randomUUID().toString());
        assertNull(foundArticle);
    }

    @Test
    @DisplayName("모든 Article을 조회하는 기능 테스트")
    public void testFindAll() {
        articleDatabase.save(article);
        Map<String, Article> allArticles = articleDatabase.findAll();

        assertNotNull(allArticles);
        assertEquals(1, allArticles.size());
        Article foundArticle = allArticles.values().iterator().next();
        assertEquals(article.getWriter(), foundArticle.getWriter());
        assertEquals(article.getTitle(), foundArticle.getTitle());
    }
}