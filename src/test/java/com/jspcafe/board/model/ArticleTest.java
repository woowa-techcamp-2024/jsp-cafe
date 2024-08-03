package com.jspcafe.board.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ArticleTest {

  @Test
  void 게시글_객체를_정적으로_생성한다() {
    // Given
    String userId = "testUserId";
    String title = "testTitle";
    String nickname = "testName";
    String content = "test test test.";

    // When
    Article article = Article.create(userId, title, nickname, content);

    // Then
    assertNotNull(article.id());
    assertEquals(userId, article.userId());
    assertEquals(title, article.title());
    assertEquals(content, article.content());
    assertEquals(nickname, article.nickname());
    assertNotNull(article.createAt());
    assertNotNull(article.updateAt());
  }

  @Test
  void 유효하지_않은_userId면_예외를_발생한다() {
    // Given
    String userId = "";
    String title = "testTitle";
    String nickname = "testName";
    String content = "test test test.";

    // When Then
    assertThrows(IllegalArgumentException.class,
        () -> Article.create(userId, title, nickname, content));
  }

  @Test
  void 유효하지_않은_제목이면_예외를_발생한다() {
    // Given
    String userId = "testUserId";
    String title = "";
    String nickname = "testName";
    String content = "test test test.";

    // When Then
    assertThrows(IllegalArgumentException.class,
        () -> Article.create(userId, title, nickname, content));
  }

  @Test
  void 유효하지_않은_내용이면_예외를_발생한다() {
    // Given
    String userId = "testUserId";
    String title = "testTitle";
    String nickname = "testName";
    String content = null;

    // When Then
    assertThrows(IllegalArgumentException.class,
        () -> Article.create(userId, title, nickname, content));
  }

  @Test
  void 유효하지_않은_id값을_입력시_예외를_발생시킨다() {
    // Given
    String id = null;
    String userId = "testUserId";
    String title = "testTitle";
    String nickname = "testName";
    String content = "test test test.";
    LocalDateTime now = LocalDateTime.now();

    // When Then
    assertThrows(IllegalArgumentException.class,
        () -> new Article(id, userId, title, nickname, content, now, now));
  }

  @Test
  void 수정할_경우_내용을_수정하고_수정날짜를_현재날짜로_변경한다() {
    // Given
    String userId = "testUserId";
    Article article = new Article("testId", userId, "testTitle", "testNickname", "test test...",
        LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(2));

    // When
    Article updateArticle = article.update("updateTitle", "updateContent");

    // Then
    assertEquals(article.id(), updateArticle.id());
    assertEquals(article.userId(), updateArticle.userId());
    assertEquals("updateTitle", updateArticle.title());
    assertEquals("updateContent", updateArticle.content());
    assertEquals(LocalDateTime.now().getDayOfYear(), updateArticle.updateAt().getDayOfYear());
  }

  @Test
  void userId가_변경되지_않음을_확인한다() {
    // Given
    String userId = "testUserId";
    Article article = Article.create(userId, "testTitle", "testNickname", "test content");

    // When
    Article updatedArticle = article.update("newTitle", "new content");

    // Then
    assertEquals(userId, updatedArticle.userId());
  }
}
