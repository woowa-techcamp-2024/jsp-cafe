package org.example.jspcafe.comment.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentTest {

    @DisplayName("정상적으로 Comment 객체를 생성한다.")
    @Test
    void createComment() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        String content = "댓글 내용";
        LocalDateTime createdAt = LocalDateTime.of(2021, 1, 1, 0, 0);

        // when
        Comment comment = new Comment(postId, userId, content, createdAt);

        // then
        assertThat(comment)
                .isNotNull()
                .extracting("postId", "userId", "content.value", "createdAt")
                .containsExactly(postId, userId, content, createdAt);
    }

    @DisplayName("게시글 ID가 null이면 IllegalArgumentException이 발생한다.")
    @Test
    void validatePostIdWithNull() {
        // given
        Long postId = null;
        Long userId = 1L;
        String content = "댓글 내용";
        LocalDateTime createdAt = LocalDateTime.of(2021, 1, 1, 0, 0);

        // when & then
        assertThatThrownBy(() -> new Comment(postId, userId, content, createdAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글 ID가 없습니다.");
    }

    @DisplayName("사용자 ID가 null이면 IllegalArgumentException이 발생한다.")
    @Test
    void validateUserIdWithNull() {
        // given
        Long postId = 1L;
        Long userId = null;
        String content = "댓글 내용";
        LocalDateTime createdAt = LocalDateTime.of(2021, 1, 1, 0, 0);

        // when & then
        assertThatThrownBy(() -> new Comment(postId, userId, content, createdAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자 ID가 없습니다.");
    }

    @DisplayName("생성일이 null이면 IllegalArgumentException이 발생한다.")
    @Test
    void validateCreatedAtWithNull() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        String content = "댓글 내용";
        LocalDateTime createdAt = null;

        // when & then
        assertThatThrownBy(() -> new Comment(postId, userId, content, createdAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("생성일이 없습니다.");
    }

    @DisplayName("댓글을 수정할 수 있다.")
    @Test
    void modifyComment() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        String content = "댓글 내용";
        LocalDateTime createdAt = LocalDateTime.of(2021, 1, 1, 0, 0);
        Comment comment = new Comment(postId, userId, content, createdAt);

        String modifiedContent = "수정된 댓글 내용";

        // when
        comment.modifyContent(modifiedContent);

        // then
        assertThat(comment)
                .extracting("postId", "userId", "content.value", "createdAt")
                .containsExactly(postId, userId, modifiedContent, createdAt);
    }

}