package cafe.service;

import cafe.domain.db.ArticleDatabase;
import cafe.domain.entity.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ArticleServiceTest {
    private ArticleDatabase articleDatabase;
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        articleDatabase = Mockito.mock(ArticleDatabase.class);
        articleService = new ArticleService(articleDatabase);
    }

    @Test
    @DisplayName("기사 저장")
    void save() {
        articleService.save("작성자", "제목", "내용");

        verify(articleDatabase, times(1)).insert(any(Article.class));
    }

    @Test
    @DisplayName("ID로 기사 조회")
    void find() {
        Article article = Article.of("작성자", "제목", "내용");
        when(articleDatabase.selectById("1")).thenReturn(article);

        Article foundArticle = articleService.find("/articles/1");

        assertNotNull(foundArticle);
        assertEquals("작성자", foundArticle.getWriter());
        assertEquals("제목", foundArticle.getTitle());
        assertEquals("내용", foundArticle.getContents());
    }

    @Test
    @DisplayName("모든 기사 조회")
    void findAll() {
        Map<String, Article> articles = new HashMap<>();
        articles.put("1", Article.of("작성자1", "제목1", "내용1"));
        articles.put("2", Article.of("작성자2", "제목2", "내용2"));
        when(articleDatabase.selectAll()).thenReturn(articles);

        Map<String, Article> foundArticles = articleService.findAll();

        assertNotNull(foundArticles);
        assertEquals(2, foundArticles.size());
        assertEquals("제목1", foundArticles.get("1").getTitle());
        assertEquals("제목2", foundArticles.get("2").getTitle());
    }

    @Test
    @DisplayName("존재하지 않는 ID로 기사 조회시 예외 발생")
    void findNonExistent() {
        when(articleDatabase.selectById("1")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            articleService.find("/articles/1");
        });
    }
}
