package org.example.jspcafe.comment.model;

import org.example.jspcafe.PK;

import java.time.LocalDateTime;

public class Comment {

    @PK
    private Long commentId;
    private final Long postId;
    private final Long userId;
    private final CommentContent content;
    private LocalDateTime createdAt;

    public Comment(
            Long postId,
            Long userId,
            String content,
            LocalDateTime createdAt
    ) {
        validatePostId(postId);
        this.postId = postId;
        validateUserId(userId);
        this.userId = userId;
        this.content = new CommentContent(content);
        validateCreatedAt(createdAt);
        this.createdAt = createdAt;
    }

    private void validatePostId(Long postId) {
        if (postId == null) {
            throw new IllegalArgumentException("게시글 ID가 없습니다.");
        }
    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID가 없습니다.");
        }
    }

    private void validateCreatedAt(LocalDateTime createdAt) {
        if (createdAt == null) {
            throw new IllegalArgumentException("생성일이 없습니다.");
        }
    }
}
