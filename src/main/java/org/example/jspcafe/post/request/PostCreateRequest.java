package org.example.jspcafe.post.request;

public record PostCreateRequest (
        Long userId,
        String title,
        String content
) {
}