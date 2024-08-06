package com.jspcafe.board.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jspcafe.exception.ArticleNotFoundException;
import com.jspcafe.test_util.H2Connector;
import com.jspcafe.test_util.H2Initializer;
import com.jspcafe.util.DatabaseConnector;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArticleDaoTest {

  private ArticleDao articleDao;
  private ReplyDao replyDao;
  private DatabaseConnector connector;

  @BeforeEach
  void setUp() throws Exception {
    connector = H2Connector.INSTANCE;
    H2Initializer.initializeDatabase(connector);
    articleDao = new ArticleDao(connector);
    replyDao = new ReplyDao(connector);
  }

  @Test
  void 게시글을_정상적으로_저장한다() {
    // Given
    String userId = "testUserId";
    Article article = Article.create(userId, "testTitle", "testName", "test test test.");

    // When
    articleDao.save(article);

    // Then
    assertEquals(article, articleDao.findById(article.id())
        .orElseThrow(() -> new ArticleNotFoundException("Article id not found")));
  }

  @Test
  void 모든_게시글의_목록을_조회한다() {
    // Given
    String userId1 = "testUserId1";
    String userId2 = "testUserId2";
    Article article1 = Article.create(userId1, "testTitle1", "testName1", "test test test. 1");
    Article article2 = Article.create(userId2, "testTitle2", "testName2", "test test test. 2");

    // When
    articleDao.save(article1);
    articleDao.save(article2);

    // Then
    assertEquals(2, articleDao.findAll(1, 15).size());
  }

  @Test
  void 게시물을_수정한다() {
    // Given
    String userId = "testUserId";
    Article article = Article.create(userId, "testTitle1", "testName1", "test test test. 1");
    articleDao.save(article);

    // When
    Article updateArticle = article.update("testTitle2", "test test test. 2");
    articleDao.update(updateArticle);

    // Then
    Article foundArticle = articleDao.findById(article.id())
        .orElseThrow(() -> new ArticleNotFoundException("Not found id"));
    assertEquals(updateArticle, foundArticle);
    assertEquals("testTitle2", foundArticle.title());
    assertEquals("test test test. 2", foundArticle.content());
  }

  @Test
  void 게시물을_삭제한다() {
    // Given
    String userId = "testUserId";
    Article article = Article.create(userId, "testTitle1", "testName1", "test test test. 1");
    articleDao.save(article);

    // When
    boolean deleted = articleDao.delete(article.id(), userId);

    // Then
    assertTrue(deleted);
    assertEquals(Optional.empty(), articleDao.findById(article.id()));
  }

  @Test
  void 다른_사용자의_게시물은_삭제할_수_없다() {
    // Given
    String userId = "testUserId";
    Article article = Article.create(userId, "testTitle1", "testName1", "test test test. 1");
    articleDao.save(article);

    // When
    boolean deleted = articleDao.delete(article.id(), "differentUserId");

    // Then
    assertFalse(deleted);
    assertTrue(articleDao.findById(article.id()).isPresent());
  }

  @Test
  void 다른_사용자의_게시물은_수정할_수_없다() {
    // Given
    String userId = "testUserId";
    Article article = Article.create(userId, "testTitle1", "testName1", "test test test. 1");
    articleDao.save(article);

    // When
    Article updateArticle = new Article(article.id(), "differentUserId", "testTitle2",
        article.nickname(), "test test test. 2", article.createAt(), article.updateAt());

    // Then
    assertThrows(RuntimeException.class, () -> articleDao.update(updateArticle));
    Article foundArticle = articleDao.findById(article.id())
        .orElseThrow(() -> new ArticleNotFoundException("Not found id"));
    assertEquals("testTitle1", foundArticle.title());
    assertEquals("test test test. 1", foundArticle.content());
  }


  @Test
  void 댓글이_없는_게시글을_삭제한다() {
    // Given
    String userId = "user1";
    Article article = Article.create(userId, "Test Title", "Nickname", "Test Content");
    articleDao.save(article);

    // When
    boolean result = articleDao.delete(article.id(), userId);

    // Then
    assertTrue(result);
    assertTrue(articleDao.findById(article.id()).isEmpty());
  }

  @Test
  void 다른_사용자의_게시글은_삭제할_수_없다() {
    // Given
    String userId = "user1";
    Article article = Article.create(userId, "Test Title", "Nickname", "Test Content");
    articleDao.save(article);

    // When
    boolean result = articleDao.delete(article.id(), "user2");

    // Then
    assertFalse(result);
    assertTrue(articleDao.findById(article.id()).isPresent());
  }

  @Test
  void 다른_사용자의_댓글이_있는_게시글은_삭제할_수_없다() {
    // Given
    String userId = "user1";
    Article article = Article.create(userId, "Test Title", "Nickname", "Test Content");
    articleDao.save(article);

    Reply reply = Reply.create(article.id(), "user2", "Replier", "Test Reply");
    replyDao.save(reply);

    // When
    boolean result = articleDao.delete(article.id(), userId);

    // Then
    assertFalse(result);
    assertTrue(articleDao.findById(article.id()).isPresent());
    assertFalse(replyDao.findByArticleId(article.id(), 1, 5).isEmpty());
  }

  @Test
  void 같은_사용자의_댓글만_있는_게시글은_모두_삭제할_수_있다() {
    // Given
    String userId = "user1";
    Article article = Article.create(userId, "Test Title", "Nickname", "Test Content");
    articleDao.save(article);

    Reply reply1 = Reply.create(article.id(), userId, "Nickname", "Test Reply 1");
    Reply reply2 = Reply.create(article.id(), userId, "Nickname", "Test Reply 2");
    replyDao.save(reply1);
    replyDao.save(reply2);

    // When
    boolean result = articleDao.delete(article.id(), userId);

    // Then
    assertTrue(result);
    assertTrue(articleDao.findById(article.id()).isEmpty());
    assertTrue(replyDao.findByArticleId(article.id(), 1, 5).isEmpty());
  }
}
