package com.hyeonuk.jspcafe.reply.domain;

import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Reply 도메인 객체 테스트")
public class ReplyTest {

    @Nested
    @DisplayName("생성자 및 Getter/Setter")
    class ConstructorAndAccessors {

        @Test
        @DisplayName("Reply 객체 생성 및 필드 접근")
        void createAndAccessFields() {
            // Given
            Long id = 1L;
            Member member = new Member(3L, "username", "password", "nickname", "email@example.com");
            Article article = new Article(2L, member, "Content", "Author");
            String contents = "This is a reply.";
            Date deletedAt = new Date();

            // When
            Reply reply = new Reply(id, article, member, contents, deletedAt);

            // Then
            assertEquals(id, reply.getId());
            assertEquals(article, reply.getArticle());
            assertEquals(member, reply.getMember());
            assertEquals(contents, reply.getContents());
            assertEquals(deletedAt, reply.getDeletedAt());
        }

        @Test
        @DisplayName("Reply 객체 Setter 테스트")
        void setFields() {
            // Given
            Reply reply = new Reply(null, null, null, null, null);

            // When
            Long id = 1L;
            Member member = new Member(3L, "username", "password", "nickname", "email@example.com");
            Article article = new Article(2L, member, "Content", "Author");
            String contents = "This is a reply.";
            Date deletedAt = new Date();

            reply.setId(id);
            reply.setArticle(article);
            reply.setMember(member);
            reply.setContents(contents);
            reply.setDeletedAt(deletedAt);

            // Then
            assertEquals(id, reply.getId());
            assertEquals(article, reply.getArticle());
            assertEquals(member, reply.getMember());
            assertEquals(contents, reply.getContents());
            assertEquals(deletedAt, reply.getDeletedAt());
        }

        @Test
        @DisplayName("Reply 객체 생성자 오버로딩 테스트")
        void constructorOverloading() {
            // Given
            Member member = new Member(3L, "username", "password", "nickname", "email@example.com");
            Article article = new Article(2L, member,"Title", "Content" );
            String contents = "This is a reply.";

            // When
            Reply reply1 = new Reply(1L, article, member, contents);
            Reply reply2 = new Reply(article, member, contents);

            // Then
            assertNotNull(reply1);
            assertNotNull(reply2);
            assertNull(reply2.getId());
            assertEquals(article, reply2.getArticle());
            assertEquals(member, reply2.getMember());
            assertEquals(contents, reply2.getContents());
        }
    }

    @Nested
    @DisplayName("Interaction: 유효성 검사")
    class Validation {

        @Test
        @DisplayName("유효한 Reply 객체")
        void validReply() {
            // Given
            Member member = new Member(3L, "username", "password", "nickname", "email@example.com");
            Article article = new Article(2L, member,"Title", "Content");
            String contents = "This is a valid reply.";
            Reply reply = new Reply(1L, article, member, contents);

            // When
            boolean isValid = reply.validation();

            // Then
            assertTrue(isValid, "Reply 객체가 유효해야 함");
        }

        @Test
        @DisplayName("ID가 null일 경우 유효성 검사 실패")
        void invalidReplyWithoutId() {
            // Given
            Member member = new Member(3L, "username", "password", "nickname", "email@example.com");
            Article article = new Article(2L, member,"Title", "Content");
            String contents = "This is a valid reply.";
            Reply reply = new Reply(null, article, member, contents);

            // When
            boolean isValid = reply.validation();

            // Then
            assertFalse(isValid, "ID가 없으면 Reply 객체가 유효하지 않음");
        }

        @Test
        @DisplayName("Article이 null일 경우 유효성 검사 실패")
        void invalidReplyWithoutArticle() {
            // Given
            Member member = new Member(3L, "username", "password", "nickname", "email@example.com");
            String contents = "This is a valid reply.";
            Reply reply = new Reply(1L, null, member, contents);

            // When
            boolean isValid = reply.validation();

            // Then
            assertFalse(isValid, "Article이 없으면 Reply 객체가 유효하지 않음");
        }

        @Test
        @DisplayName("Article의 id가 null일 경우 유효성 검사 실패")
        void invalidReplyWithoutArticleId() {
            // Given
            Member member = new Member(3L, "username", "password", "nickname", "email@example.com");
            String contents = "This is a valid reply.";
            Reply reply = new Reply(1L, new Article(member,"title","contents"), member, contents);

            // When
            boolean isValid = reply.validation();

            // Then
            assertFalse(isValid, "Article이 없으면 Reply 객체가 유효하지 않음");
        }

        @Test
        @DisplayName("Member가 null일 경우 유효성 검사 실패")
        void invalidReplyWithoutMember() {
            // Given
            Member member = new Member(3L, "username", "password", "nickname", "email@example.com");
            Article article = new Article(2L,member,"Title", "Content");
            String contents = "This is a valid reply.";
            Reply reply = new Reply(1L, article, null, contents);

            // When
            boolean isValid = reply.validation();

            // Then
            assertFalse(isValid, "Member가 없으면 Reply 객체가 유효하지 않음");
        }

        @Test
        @DisplayName("Member의 id가 null인 경우 유효성 검사 실패")
        void invalidReplyWithoutMemberId() {
            // Given
            Member member = new Member(3L, "username", "password", "nickname", "email@example.com");
            Article article = new Article(2L,member,"Title", "Content");
            String contents = "This is a valid reply.";
            Member idNullMember = new Member("username","password","nickname2","email");
            Reply reply = new Reply(1L, article, idNullMember, contents);

            // When
            boolean isValid = reply.validation();

            // Then
            assertFalse(isValid, "Member가 없으면 Reply 객체가 유효하지 않음");
        }

        @Test
        @DisplayName("Contents가 null이거나 비어 있을 경우 유효성 검사 실패")
        void invalidReplyWithoutContents() {
            // Given
            Member member = new Member(3L, "username", "password", "nickname", "email@example.com");
            Article article = new Article(2L, member,"Title", "Content");

            // When
            Reply reply1 = new Reply(1L, article, member, null);
            Reply reply2 = new Reply(1L, article, member, "   ");

            boolean isValid1 = reply1.validation();
            boolean isValid2 = reply2.validation();

            // Then
            assertFalse(isValid1, "Contents가 null이면 Reply 객체가 유효하지 않음");
            assertFalse(isValid2, "Contents가 빈 문자열이면 Reply 객체가 유효하지 않음");
        }
    }
}
