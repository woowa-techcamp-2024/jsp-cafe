package com.woowa.hyeonsik.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {

    @Test
    @DisplayName("게시글을 정상적으로 생성한다.")
    void create() {
        Article article = new Article(1L, "작성자", "제목", "내용");
        assertNotNull(article);
        assertEquals("작성자", article.writer());
        assertEquals("제목", article.title());
        assertEquals("내용", article.contents());
    }

    @ParameterizedTest
    @MethodSource("generateArticleData")
    @DisplayName("writer, title, contents가 빈 값이면 예외가 발생한다.")
    void exception_create(Long id, String writer, String title, String contents) {
        assertThrows(IllegalArgumentException.class, () -> new Article(id, writer, title, contents));
    }

    private static Stream<Arguments> generateArticleData() {
        return Stream.of(
                Arguments.of(1L, "", "제목", "내용"),
                Arguments.of(1L, "작성자", "", "내용"),
                Arguments.of(1L, "작성자", "제목", ""),
                Arguments.of(1L, null, "제목", "내용"),
                Arguments.of(1L, "작성자", null, "내용"),
                Arguments.of(1L, "작성자", "제목", null)
        );
    }

    @Test
    @DisplayName("작성자가 30글자를 초과하면 예외가 발생한다.")
    void exception_writer_length() {
        String longWriter = "a".repeat(31);
        assertThrows(IllegalArgumentException.class, () ->
                new Article(1L, longWriter, "제목", "내용")
        );
    }

    @Test
    @DisplayName("제목이 30글자가 초과하면 예외가 발생한다.")
    void exception_title_length() {
        String longTitle = "a".repeat(31);
        assertThrows(IllegalArgumentException.class, () ->
                new Article(1L, "작성자", longTitle, "내용")
        );
    }

    @Test
    @DisplayName("내용이 200글자가 초과하면 예외가 발생한다.")
    void exception_content_length() {
        String longContent = "a".repeat(201);
        assertThrows(IllegalArgumentException.class, () ->
                new Article(1L, "작성자", "제목", longContent)
        );
    }
}
