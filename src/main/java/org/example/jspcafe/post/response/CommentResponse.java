package org.example.jspcafe.post.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long commentId,
        Long userId,
        Long postId,
        String nickname,
        String content,
        LocalDateTime createdAt
) {
}
