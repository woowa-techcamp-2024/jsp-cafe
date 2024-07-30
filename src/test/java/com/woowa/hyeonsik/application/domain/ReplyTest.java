package com.woowa.hyeonsik.application.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReplyTest {
    @Test
    @DisplayName("유효한 입력으로 Reply 객체 생성 성공")
    void createReplyWithValidInput() {
        Long id = 1L;
        Long articleId = 1L;
        String writer = "테스터";
        String contents = "테스트 내용입니다.";
        LocalDateTime createdAt = LocalDateTime.now();

        Reply reply = new Reply(id, articleId, writer, contents, createdAt);

        assertNotNull(reply);
        assertEquals(articleId, reply.getArticleId());
        assertEquals(id, reply.getId());
        assertEquals(writer, reply.getWriter());
        assertEquals(contents, reply.getContents());
        assertEquals(createdAt, reply.getCreatedAt());
    }

    @Test
    @DisplayName("createdAt 없이 Reply 객체 생성 시 현재 시간으로 설정")
    void createReplyWithoutCreatedAt() {
        Long id = 1L;
        Long articleId = 1L;
        String writer = "테스터";
        String contents = "테스트 내용입니다.";

        Reply reply = new Reply(id, articleId, writer, contents);

        assertNotNull(reply);
        assertNotNull(reply.getCreatedAt());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("작성자가 비어있을 경우 예외 발생")
    void createReplyWithEmptyWriter(String emptyWriter) {
        // given
        Long id = 1L;
        Long articleId = 1L;
        String contents = "테스트 내용입니다.";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> new Reply(id, articleId, emptyWriter, contents));
    }

    @Test
    @DisplayName("작성자 이름이 최대 길이를 초과할 경우 예외 발생")
    void createReplyWithTooLongWriter() {
        // given
        Long id = 1L;
        Long articleId = 1L;
        String writer = "a".repeat(31);
        String contents = "테스트 내용입니다.";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> new Reply(id, articleId, writer, contents));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("내용이 비어있을 경우 예외 발생")
    void createReplyWithEmptyContents(String emptyContents) {
        // given
        Long id = 1L;
        Long articleId = 1L;
        String writer = "테스터";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> new Reply(id, articleId, writer, emptyContents));
    }

    @Test
    @DisplayName("내용이 최대 길이를 초과할 경우 예외 발생")
    void createReplyWithTooLongContents() {
        // given
        Long id = 1L;
        Long articleId = 1L;
        String writer = "테스터";
        String contents = "a".repeat(201);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> new Reply(id, articleId, writer, contents));
    }

    @Test
    @DisplayName("게시글 번호가 비어있을 경우 예외 발생")
    void createReplyWithEmptyArticleId() {
        Long id = 1L;
        String writer = "테스터";
        String contents = "테스트 내용입니다.";

        assertThrows(IllegalArgumentException.class, () -> new Reply(id, null, writer, contents));
    }

    @Test
    @DisplayName("게시글 번호가 음수(마이너스)일 경우 예외 발생")
    void createReplyWithNegativeArticleId() {
        Long id = 1L;
        String writer = "테스터";
        String contents = "테스트 내용입니다.";

        assertThrows(IllegalArgumentException.class, () -> new Reply(id, -12345L, writer, contents));
    }
}
