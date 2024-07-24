package com.jspcafe.board.service;

import com.jspcafe.board.model.Article;
import com.jspcafe.board.model.ArticleDao;
import com.jspcafe.exception.ArticleNotFoundException;
import com.jspcafe.test_util.H2Connector;
import com.jspcafe.test_util.H2Initializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArticleServiceTest {
    private ArticleDao articleDao;
    private ArticleService articleService;

    @BeforeEach
    void setUp() throws Exception {
        H2Initializer.initializeDatabase(H2Connector.INSTANCE);
        articleDao = new ArticleDao(H2Connector.INSTANCE);
        articleService = new ArticleService(articleDao);
    }

    @Test
    void 게시글을_정상적을_저장한다() {
        // Given
        String title = "testTitle";
        String nickname = "testName";
        String content = "test test test.";

        // When
        String id = articleService.write(title, nickname, content);

        // Then
        Article storedArticle = articleDao.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Article id not found"));
        assertEquals(title, storedArticle.title());
        assertEquals(nickname, storedArticle.nickname());
        assertEquals(content, storedArticle.content());
    }

    @Test
    void id값을_기준으로_게시글_정보를_가져온다() {
        // Given
        Article article = Article.create("testTitle", "testName", "test test test.");
        articleDao.save(article);

        // When
        Article storedArticle = articleService.findById(article.id());

        // Then
        assertEquals(article, storedArticle);
    }

    @Test
    void 게시글을_최신순으로_정렬하여_가져온다() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        Article article1 = new Article("1", "First Article", "user1", "Content 1", now.minusDays(2));
        Article article2 = new Article("2", "Second Article", "user2", "Content 2", now.minusDays(1));
        Article article3 = new Article("3", "Third Article", "user3", "Content 3", now);

        articleDao.save(article1);
        articleDao.save(article2);
        articleDao.save(article3);

        // When
        List<Article> articles = articleService.findAll();

        // Then
        assertEquals(3, articles.size());
        assertEquals("3", articles.get(0).id());
        assertEquals("2", articles.get(1).id());
        assertEquals("1", articles.get(2).id());
    }
}
