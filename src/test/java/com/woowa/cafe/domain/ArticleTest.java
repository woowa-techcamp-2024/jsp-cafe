package com.woowa.cafe.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {

    @Test
    @DisplayName("게시글 생성 시간을 yyyy-MM-dd HH:mm:ss 형식으로 반환한다.")
    void getFormattedCreatedAt() {
        Article article = new Article("제목", "내용", "작성자");

        String formattedCreatedAt = article.getFormattedCreatedAt();
        String formattedUpdatedAt = article.getFormattedUpdatedAt();

        assertAll(
                () -> assertNotNull(formattedCreatedAt),
                () -> assertEquals(19, formattedCreatedAt.length()),
                () -> assertNotNull(formattedUpdatedAt),
                () -> assertEquals(19, formattedUpdatedAt.length())
        );
    }

    @Test
    @DisplayName("게시글을 업데이트 한다.")
    void update() {
        Article article = new Article("제목", "내용", "작성자");

        article.update("수정된 제목", "수정된 내용");

        assertAll(
                () -> assertEquals("수정된 제목", article.getTitle()),
                () -> assertEquals("수정된 내용", article.getContents())
        );
    }
}
