package org.example.jspcafe.post.request;

public record PostModifyRequest (
        Long userId,
        Long postId,
        String title,
        String content
) {
}
