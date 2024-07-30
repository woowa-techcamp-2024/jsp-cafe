package com.hyeonuk.jspcafe.article.domain;

import com.hyeonuk.jspcafe.member.domain.Member;
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
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Article article = new Article(1L, member, "title", "contents");

            assertEquals(1L, article.getId());
            assertEquals(member, article.getWriter());
            assertEquals("title", article.getTitle());
            assertEquals("contents", article.getContents());
        }

        @Test
        @DisplayName("ID 없이 생성자는 필드가 올바르게 설정된다.")
        void noIdConstructor() {
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Article article = new Article(member, "title", "contents");

            assertNull(article.getId());
            assertEquals(member, article.getWriter());
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
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Article article = new Article(member, "title", "contents");
            article.setId(1L);

            assertEquals(1L, article.getId());
        }

        @Test
        @DisplayName("writer getter/setter는 값을 올바르게 설정하고 반환한다.")
        void writerGetterSetter() {
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Member newMember = new Member(2l,"id2","pw2","nick2","email2");
            Article article = new Article(member, "title", "contents");
            article.setWriter(newMember);

            assertEquals(newMember, article.getWriter());
        }

        @Test
        @DisplayName("title getter/setter는 값을 올바르게 설정하고 반환한다.")
        void titleGetterSetter() {
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Article article = new Article(member, "title", "contents");
            article.setTitle("newTitle");

            assertEquals("newTitle", article.getTitle());
        }

        @Test
        @DisplayName("contents getter/setter는 값을 올바르게 설정하고 반환한다.")
        void contentsGetterSetter() {
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Article article = new Article(member, "title", "contents");
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
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Article article = new Article(member, "title", "contents");

            assertTrue(article.validation());
        }

        @Test
        @DisplayName("writer가 null이면 false를 반환한다.")
        void writerIsNull() {
            Article article = new Article(null, "title", "contents");

            assertFalse(article.validation());
        }

        @Test
        @DisplayName("writer의 id가 null이면 false를 반환한다.")
        void writerIsBlank() {
            Member member = new Member(null,"id1","pw1","nick1","email1");
            Article article = new Article(member, "title", "contents");

            assertFalse(article.validation());
        }

        @Test
        @DisplayName("title이 null이면 false를 반환한다.")
        void titleIsNull() {
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Article article = new Article(member, null, "contents");

            assertFalse(article.validation());
        }

        @Test
        @DisplayName("title이 blank이면 false를 반환한다.")
        void titleIsBlank() {
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Article article = new Article(member, "   ", "contents");

            assertFalse(article.validation());
        }

        @Test
        @DisplayName("contents가 null이면 false를 반환한다.")
        void contentsIsNull() {
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Article article = new Article(member, "title", null);

            assertFalse(article.validation());
        }

        @Test
        @DisplayName("contents가 blank이면 false를 반환한다.")
        void contentsIsBlank() {
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Article article = new Article(member, "title", "   ");

            assertFalse(article.validation());
        }
    }
}
