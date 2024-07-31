package com.jspcafe.board.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Article(String id, String title, String nickname, String content, LocalDateTime createAt, LocalDateTime updateAt) {
    public static Article create(String title, String nickname, String content) {
        return new Article(UUID.randomUUID().toString(), title, nickname, content, LocalDateTime.now(), LocalDateTime.now());
    }

    public Article {
        validateId(id);
        validateTitle(title);
        validateContent(content);
    }

    private static void validateId(final String id) {
        if (id == null || id.isEmpty() || id.isBlank()) {
            throw new IllegalArgumentException("Invalid id: " + id);
        }
    }

    private static void validateTitle(final String title) {
        if (title == null || title.isEmpty() || title.isBlank()) {
            throw new IllegalArgumentException("Invalid title: " + title);
        }
    }

    private static void validateContent(final String content) {
        if (content == null || content.isEmpty() || content.isBlank()) {
            throw new IllegalArgumentException("Invalid content: " + content);
        }
    }

    public Article update(final String title, final String content) {
        return new Article(id, title, nickname, content, createAt, LocalDateTime.now());
    }
}
