package com.jspcafe.board.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jspcafe.board.model.Reply;
import com.jspcafe.board.model.ReplyDao;
import com.jspcafe.board.service.ReplyService;
import com.jspcafe.test_util.H2Connector;
import com.jspcafe.test_util.H2Initializer;
import com.jspcafe.test_util.StubHttpServletRequest;
import com.jspcafe.test_util.StubHttpServletResponse;
import com.jspcafe.test_util.StubServletConfig;
import com.jspcafe.test_util.StubServletContext;
import com.jspcafe.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReplyControllerTest {

  private ReplyController replyController;
  private StubHttpServletRequest request;
  private StubHttpServletResponse response;
  private StubServletContext servletContext;
  private ReplyDao replyDao;
  private ReplyService replyService;

  @BeforeEach
  void setUp() throws SQLException, ServletException {
    replyController = new ReplyController();
    request = new StubHttpServletRequest();
    response = new StubHttpServletResponse();
    servletContext = new StubServletContext();

    H2Initializer.initializeDatabase(H2Connector.INSTANCE);
    replyDao = new ReplyDao(H2Connector.INSTANCE);
    replyService = new ReplyService(replyDao);
    servletContext.setAttribute("replyDao", replyDao);
    servletContext.setAttribute("replyService", replyService);

    replyController.init(new StubServletConfig(servletContext));
  }

  @Test
  void 댓글_목록을_조회할_수_있다() throws ServletException, IOException {
    // Given
    String articleId = "article1";
    List<Reply> replies = Arrays.asList(
        Reply.create(articleId, "user1", "닉네임1", "댓글1"),
        Reply.create(articleId, "user2", "닉네임2", "댓글2")
    );
    for (Reply reply : replies) {
      replyService.save(reply.articleId(), reply.userId(), reply.nickname(), reply.content());
    }

    request.setParameter("articleId", articleId);
    request.setParameter("page", "1");

    // When
    replyController.doGet(request, response);

    // Then
    assertEquals("application/json", response.getContentType());
    String responseBody = response.getWriterContent();
    assertTrue(responseBody.contains("댓글1"));
    assertTrue(responseBody.contains("댓글2"));
  }

  @Test
  void 새_댓글을_작성할_수_있다() throws ServletException, IOException {
    // Given
    User user = User.create("test@test.com", "테스트 사용자", "password");
    request.getSession().setAttribute("userInfo", user);
    request.setBody("{\"articleId\":\"article1\",\"content\":\"새 댓글\"}");

    // When
    replyController.doPost(request, response);

    // Then
    assertEquals("application/json", response.getContentType());
    String responseBody = response.getWriterContent();
    assertTrue(responseBody.contains("새 댓글"));
    assertTrue(responseBody.contains("테스트 사용자"));
  }

  @Test
  void 로그인하지_않은_사용자는_댓글을_작성을_시도할시_로그인_페이지로_리다이렉트된다() throws ServletException, IOException {
    // Given
    request.setBody("{\"articleId\":\"article1\",\"content\":\"새 댓글\"}");

    // When
    replyController.doPost(request, response);

    // Then
    assertEquals("/users/login", response.getRedirectLocation());
  }

  @Test
  void 댓글을_수정할_수_있다() throws ServletException, IOException {
    // Given
    User user = User.create("test@test.com", "테스트 사용자", "password");
    Reply reply = replyService.save("article1", user.id(), user.nickname(), "원본 댓글");
    request.getSession().setAttribute("userInfo", user);
    request.setBody("{\"id\":\"" + reply.id() + "\",\"content\":\"수정된 댓글\"}");

    // When
    replyController.doPut(request, response);

    // Then
    assertEquals("application/json", response.getContentType());
    String responseBody = response.getWriterContent();
    assertTrue(responseBody.contains("수정된 댓글"));
  }

  @Test
  void 댓글_작성자가_아닐_경우_수정할_수_없다() throws ServletException, IOException {
    // Given
    User user1 = User.create("test1@test.com", "사용자1", "password");
    User user2 = User.create("test2@test.com", "사용자2", "password");
    Reply reply = replyService.save("article1", user1.id(), user1.nickname(), "원본 댓글");
    request.getSession().setAttribute("userInfo", user2);
    request.setBody("{\"id\":\"" + reply.id() + "\",\"content\":\"수정된 댓글\"}");

    // When
    replyController.doPut(request, response);

    // Then
    assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
  }

  @Test
  void 댓글을_삭제할_수_있다() throws ServletException, IOException {
    // Given
    User user = User.create("test@test.com", "테스트 사용자", "password");
    Reply reply = replyService.save("article1", user.id(), user.nickname(), "삭제될 댓글");
    request.getSession().setAttribute("userInfo", user);
    request.setPathInfo("/" + reply.id());

    // When
    replyController.doDelete(request, response);

    // Then
    assertEquals("application/json", response.getContentType());
    String responseBody = response.getWriterContent();
    assertTrue(responseBody.contains("true"));
  }

  @Test
  void 댓글_작성자가_아닐_경우_삭제할_수_없다() throws ServletException, IOException {
    // Given
    User user1 = User.create("test1@test.com", "사용자1", "password");
    User user2 = User.create("test2@test.com", "사용자2", "password");
    Reply reply = replyService.save("article1", user1.id(), user1.nickname(), "삭제될 댓글");
    request.getSession().setAttribute("userInfo", user2);
    request.setPathInfo("/" + reply.id());

    // When
    replyController.doDelete(request, response);

    // Then
    assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
  }
}
