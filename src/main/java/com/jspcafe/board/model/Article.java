package com.jspcafe.board.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Article(String id, String userId, String title, String nickname, String content,
                      LocalDateTime createAt, LocalDateTime updateAt) {

  public Article {
    validateId(id);
    validateUserId(userId);
    validateTitle(title);
    validateContent(content);
  }

  public static Article create(String userId, String title, String nickname, String content) {
    return new Article(UUID.randomUUID().toString(), userId, title, nickname, content,
        LocalDateTime.now(), LocalDateTime.now());
  }

  private static void validateId(final String id) {
    if (id == null || id.isEmpty() || id.isBlank()) {
      throw new IllegalArgumentException("Invalid id: " + id);
    }
  }

  private static void validateUserId(final String userId) {
    if (userId == null || userId.isEmpty() || userId.isBlank()) {
      throw new IllegalArgumentException("Invalid userId: " + userId);
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
    return new Article(id, userId, title, nickname, content, createAt, LocalDateTime.now());
  }
}
