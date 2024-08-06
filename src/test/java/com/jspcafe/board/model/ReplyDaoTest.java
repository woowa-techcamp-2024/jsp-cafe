package com.jspcafe.board.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jspcafe.test_util.H2Connector;
import com.jspcafe.test_util.H2Initializer;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReplyDaoTest {

  private ReplyDao replyDao;
  private ArticleDao articleDao;

  @BeforeEach
  void setUp() throws Exception {
    H2Initializer.initializeDatabase(H2Connector.INSTANCE);
    replyDao = new ReplyDao(H2Connector.INSTANCE);
    articleDao = new ArticleDao(H2Connector.INSTANCE);
  }

  @Test
  void 댓글을_정상적으로_저장한다() {
    // Given
    Article article = Article.create("testUserId", "testTitle", "testName", "test content");
    articleDao.save(article);
    Reply reply = Reply.create(article.id(), "testUserId", "testNickname", "test reply content");

    // When
    replyDao.save(reply);

    // Then
    assertEquals(reply, replyDao.findById(reply.id())
        .orElseThrow(() -> new RuntimeException("Reply not found")));
  }

  @Test
  void 특정_게시글의_모든_댓글을_조회한다() {
    // Given
    Article article = Article.create("testUserId", "testTitle", "testName", "test content");
    articleDao.save(article);
    Reply reply1 = Reply.create(article.id(), "testUserId1", "testNickname1",
        "test reply content 1");
    Reply reply2 = Reply.create(article.id(), "testUserId2", "testNickname2",
        "test reply content 2");

    // When
    replyDao.save(reply1);
    replyDao.save(reply2);

    // Then
    List<Reply> replies = replyDao.findByArticleId(article.id(), 1, 5);
    assertEquals(2, replies.size());
    assertTrue(replies.contains(reply1));
    assertTrue(replies.contains(reply2));
  }

  @Test
  void 댓글을_정상적으로_수정한다() {
    // Given
    Article article = Article.create("testUserId", "testTitle", "testName", "test content");
    articleDao.save(article);
    Reply reply = Reply.create(article.id(), "testUserId", "testNickname", "original content");
    replyDao.save(reply);

    // When
    Reply updatedReply = new Reply(reply.id(), reply.articleId(), reply.userId(), reply.nickname(),
        "updated content", reply.createAt(), LocalDateTime.now());
    replyDao.update(updatedReply);

    // Then
    Reply foundReply = replyDao.findById(reply.id())
        .orElseThrow(() -> new RuntimeException("Reply not found"));
    assertEquals("updated content", foundReply.content());
    assertTrue(foundReply.updateAt().isAfter(reply.updateAt()));
  }

  @Test
  void 댓글을_정상적으로_소프트_삭제한다() {
    // Given
    Article article = Article.create("testUserId", "testTitle", "testName", "test content");
    articleDao.save(article);
    Reply reply = Reply.create(article.id(), "testUserId", "testNickname", "test reply content");
    replyDao.save(reply);

    // When
    replyDao.softDelete(reply.id());

    // Then
    assertTrue(replyDao.findById(reply.id()).isEmpty());
    assertTrue(replyDao.findByArticleId(article.id(), 1, 5).isEmpty());
  }
}
