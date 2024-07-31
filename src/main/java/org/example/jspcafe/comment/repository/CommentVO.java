package org.example.jspcafe.comment.repository;

import java.time.LocalDateTime;

public record CommentVO(
        Long commentId,
        Long postId,
        Long userId,
        String nickname,
        String content,
        LocalDateTime createdAt

) {
}
