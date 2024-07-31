package org.example.jspcafe.comment.request;

public record CommentModifyRequest (
        Long commentId,
        Long userId,
        String content
){
}
