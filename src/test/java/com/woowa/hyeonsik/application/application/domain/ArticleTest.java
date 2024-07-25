package com.woowa.hyeonsik.application.application.domain;

import com.woowa.hyeonsik.application.domain.Article;
import java.time.LocalDateTime;
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
        assertEquals("작성자", article.getWriter());
        assertEquals("제목", article.getTitle());
        assertEquals("내용", article.getContents());
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
    void exception_getWriter_length() {
        String longWriter = "a".repeat(31);
        assertThrows(IllegalArgumentException.class, () ->
                new Article(1L, longWriter, "제목", "내용")
        );
    }

    @Test
    @DisplayName("제목이 30글자가 초과하면 예외가 발생한다.")
    void exception_getTitle_length() {
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

    @Test
    @DisplayName("동일한 Article 객체는 equals가 true를 반환해야 한다")
    void givenTwoIdenticalArticles_whenEquals_thenReturnTrue() {
        Article article1 = new Article(1L, "writer", "title", "contents", LocalDateTime.now());
        Article article2 = new Article(1L, "writer", "title", "contents", LocalDateTime.now());

        boolean isEqual = article1.equals(article2);

        assertTrue(isEqual, "동일한 Article 객체는 equals가 true를 반환해야 합니다.");
    }

    @Test
    @DisplayName("서로 다른 Article 객체는 equals가 false를 반환해야 한다")
    void givenTwoDifferentArticles_whenEquals_thenReturnFalse() {
        Article article1 = new Article(1L, "writer1", "title1", "contents1", LocalDateTime.now());
        Article article2 = new Article(2L, "writer2", "title2", "contents2", LocalDateTime.now());

        boolean isEqual = article1.equals(article2);

        assertFalse(isEqual, "서로 다른 Article 객체는 equals가 false를 반환해야 합니다.");
    }

    @ParameterizedTest
    @MethodSource("provideArticlesForHashCodeTest")
    @DisplayName("동일한 Article 객체는 동일한 hashCode를 가져야 한다")
    void givenTwoIdenticalArticles_whenHashCode_thenReturnSameHashCode(Article article1, Article article2, boolean expected) {
        int hashCode1 = article1.hashCode();
        int hashCode2 = article2.hashCode();

        if (expected) {
            assertEquals(hashCode1, hashCode2, "동일한 Article 객체는 동일한 hashCode를 가져야 합니다.");
        } else {
            assertNotEquals(hashCode1, hashCode2, "서로 다른 Article 객체는 다른 hashCode를 가져야 합니다.");
        }
    }

    private static Stream<Arguments> provideArticlesForHashCodeTest() {
        return Stream.of(
            Arguments.of(new Article(1L, "writer", "title", "contents", LocalDateTime.now()),
                new Article(1L, "writer", "title", "contents", LocalDateTime.now()), true),
            Arguments.of(new Article(1L, "writer1", "title1", "contents1", LocalDateTime.now()),
                new Article(2L, "writer2", "title2", "contents2", LocalDateTime.now()), false)
        );
    }

    @Test
    @DisplayName("Article 객체는 자기 자신과 equals 비교 시 true를 반환해야 한다")
    void givenArticle_whenEqualsItself_thenReturnTrue() {
        Article article = new Article(1L, "writer", "title", "contents", LocalDateTime.now());

        boolean isEqual = article.equals(article);

        assertTrue(isEqual, "Article 객체는 자기 자신과 equals 비교 시 true를 반환해야 합니다.");
    }

    @Test
    @DisplayName("Article 객체는 null과 equals 비교 시 false를 반환해야 한다")
    void givenArticle_whenEqualsNull_thenReturnFalse() {
        Article article = new Article(1L, "writer", "title", "contents", LocalDateTime.now());

        boolean isEqual = article.equals(null);

        assertFalse(isEqual, "Article 객체는 null과 equals 비교 시 false를 반환해야 합니다.");
    }
}
