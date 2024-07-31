package com.jspcafe.board.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record Reply(String id, String articleId, String userId, String nickname, String content,
                    LocalDateTime createAt, LocalDateTime updateAt) {

  public Reply {
    validateId(id);
    validateArticleId(articleId);
    validateUserId(userId);
    validateNickname(nickname);
    validateContent(content);
  }

  public static Reply create(String articleId, String userId, String nickname, String content) {
    return new Reply(UUID.randomUUID().toString(), articleId, userId, nickname, content,
        LocalDateTime.now(), LocalDateTime.now());
  }

  private static void validateId(final String id) {
    if (id == null || id.isEmpty() || id.isBlank()) {
      throw new IllegalArgumentException("Invalid id: " + id);
    }
  }

  private static void validateArticleId(final String articleId) {
    if (articleId == null || articleId.isEmpty() || articleId.isBlank()) {
      throw new IllegalArgumentException("Invalid articleId: " + articleId);
    }
  }

  private static void validateUserId(final String userId) {
    if (userId == null || userId.isEmpty() || userId.isBlank()) {
      throw new IllegalArgumentException("Invalid userId: " + userId);
    }
  }

  private static void validateNickname(final String nickname) {
    if (nickname == null || nickname.isEmpty() || nickname.isBlank()) {
      throw new IllegalArgumentException("Invalid nickname: " + nickname);
    }
  }

  private static void validateContent(final String content) {
    if (content == null || content.isEmpty() || content.isBlank()) {
      throw new IllegalArgumentException("Invalid content: " + content);
    }
  }

  public Reply update(final String content) {
    return new Reply(id, articleId, userId, nickname, content, createAt, LocalDateTime.now());
  }
}
