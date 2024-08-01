package org.example.reply.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.example.member.model.dto.UserDto;
import org.example.reply.model.ReplyStatus;
import org.example.reply.model.dao.Reply;
import org.example.reply.model.dto.ReplyDto;
import org.example.reply.repository.ReplyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReplyServiceTest {

    @Mock
    private ReplyRepository replyRepository;

    @InjectMocks
    private ReplyService replyService;

    @Test
    @DisplayName("올바른 게시물 ID로 모든 댓글을 성공적으로 조회한다")
    public void test_get_all_replies_success() throws SQLException {
        // Given
        Long postId = 1L;
        List<ReplyDto> expectedReplies = List.of(new ReplyDto.Builder().id(1L).postId(postId).build());
        when(replyRepository.findAll(postId)).thenReturn(expectedReplies);

        // When
        List<ReplyDto> actualReplies = replyService.getAllReplies(postId);

        // Then
        assertEquals(expectedReplies, actualReplies);
    }

    @Test
    @DisplayName("유효한 댓글 ID로 특정 댓글을 성공적으로 조회한다")
    public void test_get_reply_by_id_success() throws SQLException {
        // Given
        Long replyId = 1L;
        ReplyDto expectedReply = new ReplyDto.Builder().id(replyId).build();
        when(replyRepository.findById(replyId)).thenReturn(expectedReply);

        // When
        ReplyDto actualReply = replyService.getReplyById(replyId);

        // Then
        assertEquals(expectedReply, actualReply);
    }

    @Test
    @DisplayName("유효한 사용자와 게시물 ID로 새로운 댓글을 성공적으로 저장한다")
    public void test_save_reply_success() throws SQLException {
        // Given
        UserDto user = UserDto.createUserResponseDto("user1", "User One", "user1@example.com");
        Long postId = 1L;
        String contents = "This is a reply";
        Reply reply = Reply.create(user.getUserId(), postId, contents);
        Reply savedReply = Reply.createWithAll(1L, postId, user.getUserId(), contents, ReplyStatus.AVAILABLE,
                LocalDateTime.now());
        when(replyRepository.save(any(Reply.class))).thenReturn(savedReply);

        // When
        ReplyDto actualReply = replyService.saveReply(user, postId, contents);

        // Then
        assertNotNull(actualReply);
        assertEquals(savedReply.getId(), actualReply.getId());
        verify(replyRepository).save(any(Reply.class));
    }

    @Test
    @DisplayName("유효한 사용자와 댓글 ID로 기존 댓글을 성공적으로 수정한다")
    public void test_update_reply_success() throws SQLException {
        // Given
        Long replyId = 1L;
        UserDto user = UserDto.createUserResponseDto("user1", "User One", "user1@example.com");
        String newContents = "Updated contents";
        ReplyDto existingReply = new ReplyDto.Builder().id(replyId).userId(user.getUserId()).contents("Old contents")
                .build();
        ReplyDto updatedReply = new ReplyDto.Builder().id(replyId).userId(user.getUserId()).contents(newContents)
                .build();
        when(replyRepository.findById(replyId)).thenReturn(existingReply);
        when(replyRepository.update(existingReply)).thenReturn(updatedReply);

        // When
        ReplyDto actualReply = replyService.updateReply(replyId, user, newContents);

        // Then
        assertEquals(newContents, actualReply.getContents());
    }

    @Test
    @DisplayName("유효한 사용자와 댓글 ID로 댓글을 성공적으로 삭제한다")
    public void test_delete_reply_success() throws SQLException {
        // Given
        Long replyId = 1L;
        UserDto user = UserDto.createUserResponseDto("user1", "User One", "user1@example.com");
        ReplyDto existingReply = new ReplyDto.Builder().id(replyId).userId(user.getUserId()).build();
        when(replyRepository.findById(replyId)).thenReturn(existingReply);
        doNothing().when(replyRepository).delete(replyId);

        // When
        boolean isDeleted = replyService.deleteReply(replyId, user);

        // Then
        assertTrue(isDeleted);
    }

    @Test
    @DisplayName("존재하지 않는 게시물 ID로 댓글 조회 시 빈 목록을 반환한다")
    public void test_get_all_replies_non_existent_post_id() throws SQLException {
        // Given
        Long postId = 999L;
        when(replyRepository.findAll(postId)).thenReturn(Collections.emptyList());

        // When
        List<ReplyDto> actualReplies = replyService.getAllReplies(postId);

        // Then
        assertTrue(actualReplies.isEmpty());
    }

    @Test
    @DisplayName("존재하지 않는 댓글 ID로 조회 시 예외를 발생시킨다")
    public void test_get_reply_by_non_existent_id() throws SQLException {
        // Given
        Long replyId = 999L;
        when(replyRepository.findById(replyId)).thenThrow(new SQLException("Reply not found"));

        // When & Then
        assertThrows(SQLException.class, () -> {
            replyService.getReplyById(replyId);
        });
    }

    @Test
    @DisplayName("유효하지 않은 사용자 ID로 댓글 저장 시 예외를 발생시킨다")
    public void test_save_reply_invalid_user_id() throws SQLException {
        // Given
        UserDto user = UserDto.createUserResponseDto(null, "User One", "user1@example.com");
        Long postId = 1L;
        String contents = "This is a reply";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            Reply.create(user.getUserId(), postId, contents);
        });
    }
}