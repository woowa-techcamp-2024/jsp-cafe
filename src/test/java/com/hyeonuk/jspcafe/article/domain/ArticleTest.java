package com.hyeonuk.jspcafe.article.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Article 클래스")
class ArticleTest {

    @Nested
    @DisplayName("생성자")
    class Constructor {

        @Test
        @DisplayName("모든 필드를 포함한 생성자는 필드가 올바르게 설정된다.")
        void allFieldsConstructor() {
            Article article = new Article(1L, "writer", "title", "contents");

            assertEquals(1L, article.getId());
            assertEquals("writer", article.getWriter());
            assertEquals("title", article.getTitle());
            assertEquals("contents", article.getContents());
        }

        @Test
        @DisplayName("ID 없이 생성자는 필드가 올바르게 설정된다.")
        void noIdConstructor() {
            Article article = new Article("writer", "title", "contents");

            assertNull(article.getId());
            assertEquals("writer", article.getWriter());
            assertEquals("title", article.getTitle());
            assertEquals("contents", article.getContents());
        }
    }

    @Nested
    @DisplayName("getter/setter 메서드")
    class GetterSetter {

        @Test
        @DisplayName("ID getter/setter는 값을 올바르게 설정하고 반환한다.")
        void idGetterSetter() {
            Article article = new Article("writer", "title", "contents");
            article.setId(1L);

            assertEquals(1L, article.getId());
        }

        @Test
        @DisplayName("writer getter/setter는 값을 올바르게 설정하고 반환한다.")
        void writerGetterSetter() {
            Article article = new Article("writer", "title", "contents");
            article.setWriter("newWriter");

            assertEquals("newWriter", article.getWriter());
        }

        @Test
        @DisplayName("title getter/setter는 값을 올바르게 설정하고 반환한다.")
        void titleGetterSetter() {
            Article article = new Article("writer", "title", "contents");
            article.setTitle("newTitle");

            assertEquals("newTitle", article.getTitle());
        }

        @Test
        @DisplayName("contents getter/setter는 값을 올바르게 설정하고 반환한다.")
        void contentsGetterSetter() {
            Article article = new Article("writer", "title", "contents");
            article.setContents("newContents");

            assertEquals("newContents", article.getContents());
        }
    }

    @Nested
    @DisplayName("validation 메서드")
    class Validation {

        @Test
        @DisplayName("유효한 Article 객체는 true를 반환한다.")
        void validArticle() {
            Article article = new Article("writer", "title", "contents");

            assertTrue(article.validation());
        }

        @Test
        @DisplayName("writer가 null이면 false를 반환한다.")
        void writerIsNull() {
            Article article = new Article(null, "title", "contents");

            assertFalse(article.validation());
        }

        @Test
        @DisplayName("writer가 blank이면 false를 반환한다.")
        void writerIsBlank() {
            Article article = new Article("   ", "title", "contents");

            assertFalse(article.validation());
        }

        @Test
        @DisplayName("title이 null이면 false를 반환한다.")
        void titleIsNull() {
            Article article = new Article("writer", null, "contents");

            assertFalse(article.validation());
        }

        @Test
        @DisplayName("title이 blank이면 false를 반환한다.")
        void titleIsBlank() {
            Article article = new Article("writer", "   ", "contents");

            assertFalse(article.validation());
        }

        @Test
        @DisplayName("contents가 null이면 false를 반환한다.")
        void contentsIsNull() {
            Article article = new Article("writer", "title", null);

            assertFalse(article.validation());
        }

        @Test
        @DisplayName("contents가 blank이면 false를 반환한다.")
        void contentsIsBlank() {
            Article article = new Article("writer", "title", "   ");

            assertFalse(article.validation());
        }
    }
}
