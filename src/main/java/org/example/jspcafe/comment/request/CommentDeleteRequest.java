package org.example.jspcafe.comment.request;

public record CommentDeleteRequest (
        Long commentId,
        Long userId
) {
}
