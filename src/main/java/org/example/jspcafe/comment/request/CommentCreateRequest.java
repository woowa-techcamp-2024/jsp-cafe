package org.example.jspcafe.comment.request;

public record CommentCreateRequest(
        Long postId,
        Long userId,
        String content
) {
}
