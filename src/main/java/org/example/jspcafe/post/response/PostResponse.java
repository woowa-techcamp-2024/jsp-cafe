package org.example.jspcafe.post.response;

import java.time.LocalDateTime;

public record PostResponse(
        Long postId,
        Long userId,
        String nickname,
        String title,
        String content,
        LocalDateTime createdAt
) {
}