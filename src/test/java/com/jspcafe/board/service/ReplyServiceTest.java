package com.jspcafe.board.service;

import com.jspcafe.board.model.Reply;
import com.jspcafe.board.model.ReplyDao;
import com.jspcafe.exception.ReplyNotFoundException;
import com.jspcafe.test_util.H2Connector;
import com.jspcafe.test_util.H2Initializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReplyServiceTest {
    private ReplyService replyService;
    private ReplyDao replyDao;

    @BeforeEach
    void setUp() throws SQLException {
        H2Initializer.initializeDatabase(H2Connector.INSTANCE);
        replyDao = new ReplyDao(H2Connector.INSTANCE);
        replyService = new ReplyService(replyDao);
    }

    @Test
    void findByArticleId_리플라이_목록을_반환한다() {
        // Given
        String articleId = "article1";
        Reply reply1 = replyService.save(articleId, "user1", "닉네임1", "댓글1");
        Reply reply2 = replyService.save(articleId, "user2", "닉네임2", "댓글2");

        // When
        List<Reply> replies = replyService.findByArticleId(articleId);

        // Then
        assertEquals(2, replies.size());
        assertTrue(replies.stream().anyMatch(r -> r.content().equals("댓글1")));
        assertTrue(replies.stream().anyMatch(r -> r.content().equals("댓글2")));
    }

    @Test
    void findById_존재하는_리플라이를_반환한다() throws ReplyNotFoundException {
        // Given
        Reply savedReply = replyService.save("article1", "user1", "닉네임1", "댓글1");

        // When
        Reply foundReply = replyService.findById(savedReply.id());

        // Then
        assertEquals(savedReply.id(), foundReply.id());
        assertEquals(savedReply.content(), foundReply.content());
    }

    @Test
    void findById_존재하지_않는_리플라이는_예외를_발생시킨다() {
        // Given
        String nonExistentId = "nonexistent";

        // When & Then
        assertThrows(ReplyNotFoundException.class, () -> replyService.findById(nonExistentId));
    }

    @Test
    void save_새_리플라이를_저장하고_반환한다() {
        // Given
        String articleId = "article1";
        String userId = "user1";
        String nickname = "닉네임1";
        String content = "새 댓글";

        // When
        Reply savedReply = replyService.save(articleId, userId, nickname, content);

        // Then
        assertNotNull(savedReply.id());
        assertEquals(articleId, savedReply.articleId());
        assertEquals(userId, savedReply.userId());
        assertEquals(nickname, savedReply.nickname());
        assertEquals(content, savedReply.content());
    }

    @Test
    void update_리플라이를_수정하고_반환한다() throws ReplyNotFoundException {
        // Given
        Reply originalReply = replyService.save("article1", "user1", "닉네임1", "원본 댓글");
        String newContent = "수정된 댓글";

        // When
        Reply updatedReply = replyService.update(originalReply.id(), newContent);

        // Then
        assertEquals(originalReply.id(), updatedReply.id());
        assertEquals(newContent, updatedReply.content());
    }

    @Test
    void delete_리플라이를_삭제하고_true를_반환한다() {
        // Given
        Reply reply = replyService.save("article1", "user1", "닉네임1", "삭제될 댓글");

        // When
        boolean result = replyService.delete(reply.id());

        // Then
        assertTrue(result);
        assertThrows(ReplyNotFoundException.class, () -> replyService.findById(reply.id()));
    }
}
