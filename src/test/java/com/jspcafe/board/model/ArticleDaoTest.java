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
        Article article = Article.create("testTitle", "testName", "test test test.");

        // When
        articleDao.save(article);

        // Then
        assertEquals(article, articleDao.findById(article.id())
                .orElseThrow(() -> new ArticleNotFoundException("Article id not found")));
    }

    @Test
    void 모든_게시글의_목록을_조회한다() {
        // Given
        Article article1 = Article.create("testTitle1", "testTitle1", "test test test. 1");
        Article article2 = Article.create("testTitle2", "testTitle2", "test test test. 2");

        // When
        articleDao.save(article1);
        articleDao.save(article2);

        // Then
        assertEquals(2, articleDao.findAll().size());
    }
}
