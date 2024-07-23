package com.jspcafe.board.service;

import com.jspcafe.board.model.Article;
import com.jspcafe.board.model.ArticleDao;
import com.jspcafe.exception.ArticleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArticleServiceTest {
    private ArticleDao articleDao;
    private ArticleService articleService;

    @BeforeEach
    void setUp() throws Exception {
        articleDao = new ArticleDao();
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
}
