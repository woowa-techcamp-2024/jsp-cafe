package com.jspcafe.board.model;

import com.jspcafe.exception.ArticleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArticleDaoTest {
    private ArticleDao articleDao;

    @BeforeEach
    void setUp() throws Exception {
        articleDao = new ArticleDao();
    }

    @Test
    void 게시글을_정상적을_저장한다() {
        // Given
        Article article = Article.create("testTitle", "testTitle", "test test test.");

        // When
        articleDao.save(article);

        // Then
        assertEquals(article, articleDao.findById(article.id())
                .orElseThrow(() -> new ArticleNotFoundException("Article id not found")));
    }
}
