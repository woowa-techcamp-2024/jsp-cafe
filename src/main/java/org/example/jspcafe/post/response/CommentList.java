package org.example.jspcafe.post.response;

import java.util.List;

public record CommentList (
        List<CommentResponse> comments,
        int totalComments
) {
}
