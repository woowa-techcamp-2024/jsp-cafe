package com.woowa.hyeonsik.service;

import com.woowa.hyeonsik.dao.ArticleDao;
import com.woowa.hyeonsik.domain.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ArticleServiceTest {
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        ArticleDao articleDao = new ArticleDao();
        articleService = new ArticleService(articleDao);
        articleDao.clear();
    }

    @Test
    @DisplayName("정상적으로 글을 작성한다.")
    void write() {
        Article article = new Article(null, "TEST_USER", "테스트 글입니다", "안녕하세요? 반갑습니다 ^^");

        articleService.write(article);
        Article foundArticle = articleService.findById(1);

        assertThat(foundArticle.getWriter()).isEqualTo("TEST_USER");
        assertThat(foundArticle.getTitle()).isEqualTo("테스트 글입니다");
        assertThat(foundArticle.getContents()).isEqualTo("안녕하세요? 반갑습니다 ^^");
    }

    @Test
    @DisplayName("정상적으로 글 목록을 확인한다.")
    void list() {
        Article article = new Article(null, "TEST_USER", "테스트 글입니다", "안녕하세요? 반갑습니다 ^^");

        articleService.write(article);
        articleService.write(article);
        articleService.write(article);
        List<Article> list = articleService.list();

        assertThat(list).hasSize(3);
    }
}
