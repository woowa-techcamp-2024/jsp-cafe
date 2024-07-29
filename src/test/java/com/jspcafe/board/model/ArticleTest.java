package com.jspcafe.board.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {

    @Test
    void 게시글_객체를_정적으로_생성한다() {
        // Given
        String title = "testTitle";
        String nickname = "testName";
        String content = "test test test.";

        // When
        Article article = Article.create(title, nickname, content);

        // Then
        assertEquals(title, article.title());
        assertEquals(content, article.content());
        assertEquals(nickname, article.nickname());
    }

    @Test
    void 유효하지_않은_제목이면_예외를_발생한다() {
        // Given
        String title = "";
        String nickname = "testName";
        String content = "test test test.";

        // When Then
        assertThrows(IllegalArgumentException.class,
                () -> Article.create(title, nickname, content));
    }

    @Test
    void 유효하지_않은_내용이면_예외를_발생한다() {
        // Given
        String title = "testTitle";
        String nickname = "testName";
        String content = null;

        // When Then
        assertThrows(IllegalArgumentException.class,
                () -> Article.create(title, nickname, content));
    }

    @Test
    void 유효하지_않은_id값을_입력시_예외를_발생시킨다() {
        // Given
        String id = null;
        String title = "testTitle";
        String nickname = "testName";
        String content = "test test test.";
        LocalDateTime now = LocalDateTime.now();

        // When Then
        assertThrows(IllegalArgumentException.class,
                () -> new Article(id, title, nickname, content, now, now));
    }
}
