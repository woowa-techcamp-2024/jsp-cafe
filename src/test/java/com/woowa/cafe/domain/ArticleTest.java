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

        assertNotNull(formattedCreatedAt);
        assertEquals(19, formattedCreatedAt.length());
    }
}
