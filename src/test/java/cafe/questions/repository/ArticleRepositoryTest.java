package cafe.questions.repository;

import cafe.questions.Article;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ArticleRepositoryTest {
    private ArticleRepository articleRepository;

    protected abstract ArticleRepository createArticleRepository();

    @BeforeAll
    void setUp() {
        articleRepository = createArticleRepository();
    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteAll();
    }

    @Test
    public void insertArticle() {
        Article article = new Article(1L, "Content of the article", "Article Title");
        Article savedArticle = articleRepository.save(article);
        assertNotNull(savedArticle.getId());
    }

    @Test
    void updateArticle() {
        Article article = new Article(1L, "Content of the article", "Article Title");
        Article savedArticle = articleRepository.save(article);
        String updatedTitle = "Updated Title";
        String updatedContent = "Updated Content";
        Article updatedArticle = savedArticle.withTitle(updatedTitle).withContent(updatedContent);

        savedArticle = articleRepository.save(updatedArticle);

        assertEquals(savedArticle.getId(), updatedArticle.getId());
        assertEquals(updatedTitle, savedArticle.getTitle());
        assertEquals(updatedContent, savedArticle.getContent());
    }

    @Test
    public void testFindAllArticles() {
        Article article1 = new Article(1L, "Content of the first article", "First Article");
        Article article2 = new Article(2L, "Content of the second article", "Second Article");
        articleRepository.save(article1);
        articleRepository.save(article2);

        List<Article> articles = articleRepository.findAll();
        assertEquals(2, articles.size());
    }

    @Test
    public void testFindArticleById() {
        String title = "Article Title";
        String content = "Content of the article";
        Article article = new Article(1L, title, content);
        Article savedArticle = articleRepository.save(article);

        Article foundArticle = articleRepository.findById(savedArticle.getId());
        assertEquals(title, foundArticle.getTitle());
        assertEquals(content, foundArticle.getContent());
    }

    @Test
    public void testDeleteAllArticles() {
        Article article1 = new Article(1L, "Content of the first article", "First Article");
        Article article2 = new Article(2L, "Content of the second article", "Second Article");
        articleRepository.save(article1);
        articleRepository.save(article2);

        articleRepository.deleteAll();
        assertTrue(articleRepository.findAll().isEmpty());
    }

}