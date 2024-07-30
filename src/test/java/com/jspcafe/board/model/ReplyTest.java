package com.jspcafe.board.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReplyTest {
    @Test
    void 댓글_객체를_정적으로_생성한다() {
        // Given
        String articleId = "testArticleId";
        String userId = "testUserId";
        String nickname = "testName";
        String content = "test reply content.";

        // When
        Reply reply = Reply.create(articleId, userId, nickname, content);

        // Then
        assertEquals(articleId, reply.articleId());
        assertEquals(userId, reply.userId());
        assertEquals(nickname, reply.nickname());
        assertEquals(content, reply.content());
        assertNotNull(reply.id());
        assertNotNull(reply.createAt());
        assertNotNull(reply.updateAt());
    }

    @Test
    void 유효하지_않은_articleId이면_예외를_발생한다() {
        // Given
        String articleId = "";
        String userId = "testUserId";
        String nickname = "testName";
        String content = "test reply content.";

        // When Then
        assertThrows(IllegalArgumentException.class,
                () -> Reply.create(articleId, userId, nickname, content));
    }

    @Test
    void 유효하지_않은_userId이면_예외를_발생한다() {
        // Given
        String articleId = "testArticleId";
        String userId = null;
        String nickname = "testName";
        String content = "test reply content.";

        // When Then
        assertThrows(IllegalArgumentException.class,
                () -> Reply.create(articleId, userId, nickname, content));
    }

    @Test
    void 유효하지_않은_nickname이면_예외를_발생한다() {
        // Given
        String articleId = "testArticleId";
        String userId = "testUserId";
        String nickname = "";
        String content = "test reply content.";

        // When Then
        assertThrows(IllegalArgumentException.class,
                () -> Reply.create(articleId, userId, nickname, content));
    }

    @Test
    void 유효하지_않은_내용이면_예외를_발생한다() {
        // Given
        String articleId = "testArticleId";
        String userId = "testUserId";
        String nickname = "testName";
        String content = null;

        // When Then
        assertThrows(IllegalArgumentException.class,
                () -> Reply.create(articleId, userId, nickname, content));
    }

    @Test
    void 유효하지_않은_id값을_입력시_예외를_발생시킨다() {
        // Given
        String id = null;
        String articleId = "testArticleId";
        String userId = "testUserId";
        String nickname = "testName";
        String content = "test reply content.";
        LocalDateTime now = LocalDateTime.now();

        // When Then
        assertThrows(IllegalArgumentException.class,
                () -> new Reply(id, articleId, userId, nickname, content, now, now));
    }

    @Test
    void 수정할_경우_내용을_수정하고_수정날짜를_현재날짜로_변경한다() {
        // Given
        Reply reply = new Reply("testId", "testArticleId", "testUserId", "testNickname", "test reply...",
                LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2));

        // When
        Reply updatedReply = reply.update("updated reply content");

        // Then
        assertEquals(reply.id(), updatedReply.id());
        assertEquals(reply.articleId(), updatedReply.articleId());
        assertEquals(reply.userId(), updatedReply.userId());
        assertEquals(reply.nickname(), updatedReply.nickname());
        assertEquals("updated reply content", updatedReply.content());
        assertEquals(reply.createAt(), updatedReply.createAt());
        assertTrue(updatedReply.updateAt().isAfter(reply.updateAt()));
    }
}
