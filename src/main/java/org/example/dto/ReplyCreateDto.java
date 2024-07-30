package org.example.dto;

import org.example.entity.Reply;

public record ReplyCreateDto(String content, String authorId, int articleId) {
    public Reply toEntity() {
        return new Reply(content, authorId, articleId);
    }
}
