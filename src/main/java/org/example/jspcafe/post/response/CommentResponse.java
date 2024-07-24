package org.example.jspcafe.post.response;

import java.time.LocalDateTime;

public record CommentResponse(
        String commentId,
        String postId,
        String nickname,
        String content,
        LocalDateTime createdAt
) {
}
