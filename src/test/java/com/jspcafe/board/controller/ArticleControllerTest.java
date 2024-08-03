package com.jspcafe.board.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jspcafe.board.model.Article;
import com.jspcafe.board.model.ArticleDao;
import com.jspcafe.board.model.Reply;
import com.jspcafe.board.model.ReplyDao;
import com.jspcafe.board.service.ArticleService;
import com.jspcafe.test_util.H2Connector;
import com.jspcafe.test_util.H2Initializer;
import com.jspcafe.test_util.StubHttpServletRequest;
import com.jspcafe.test_util.StubHttpServletResponse;
import com.jspcafe.test_util.StubServletConfig;
import com.jspcafe.test_util.StubServletContext;
import com.jspcafe.user.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArticleControllerTest {

  private ArticleController articleController;
  private StubHttpServletRequest request;
  private StubHttpServletResponse response;
  private StubServletContext servletContext;
  private ArticleDao articleDao;
  private ArticleService articleService;

  @BeforeEach
  void setUp() throws SQLException {
    articleController = new ArticleController();
    request = new StubHttpServletRequest();
    response = new StubHttpServletResponse();
    servletContext = new StubServletContext();

    H2Initializer.initializeDatabase(H2Connector.INSTANCE);
    articleDao = new ArticleDao(H2Connector.INSTANCE);
    articleService = new ArticleService(articleDao);
    servletContext.setAttribute("articleService", articleService);

    articleController.init(new StubServletConfig(servletContext));
  }

  @Test
  void 게시글_목록을_조회할_수_있다() throws ServletException, IOException {
    // Given
    List<Article> articles = Arrays.asList(
        Article.create("testUserId1", "제목1", "작성자1", "내용1"),
        Article.create("testUserId2", "제목2", "작성자2", "내용2")
    );
    for (Article article : articles) {
      articleDao.save(article);
    }

    request.setPathInfo(null);

    // When
    articleController.doGet(request, response);
    List<Article> result = (List<Article>) request.getAttribute("articles");

    // Then
    assertEquals("/WEB-INF/views/board/board.jsp", request.getForwardedPath());
    assertEquals(articles.size(), result.size());
  }

  @Test
  void 특정_게시글을_조회할_수_있다() throws ServletException, IOException {
    // Given
    Article article = Article.create("testUserId", "테스트 제목", "테스트 작성자", "테스트 내용");
    articleDao.save(article);
    request.setPathInfo("/" + article.id());

    // When
    articleController.doGet(request, response);

    // Then
    assertEquals("/WEB-INF/views/board/article_detail.jsp", request.getForwardedPath());
    assertEquals(article, request.getAttribute("article"));
  }

  @Test
  void 새_게시글을_작성할_수_있다() throws ServletException, IOException {
    // Given
    User user = User.create("test@test", "새 작성자", "password");
    request.getSession().setAttribute("userInfo", user);
    request.setParameter("title", "새 게시글");
    request.setParameter("content", "새 내용");

    // When
    articleController.doPost(request, response);

    // Then
    List<Article> articles = articleService.findAll();
    assertEquals(1, articles.size());
    Article newArticle = articles.get(0);
    assertEquals("새 게시글", newArticle.title());
    assertEquals("새 작성자", newArticle.nickname());
    assertEquals("새 내용", newArticle.content());
    assertEquals("/", response.getRedirectLocation());
  }

  @Test
  void 게시글_작성_폼을_조회할_수_있다() throws ServletException, IOException {
    // Given
    request.setPathInfo("/form");

    // When
    articleController.doGet(request, response);

    // Then
    assertEquals("/WEB-INF/views/board/article_form.jsp", request.getForwardedPath());
  }

  @Test
  void 게시글_수정폼을_요청할_수_있다() throws ServletException, IOException {
    // Given
    User user = User.create("test@test", "테스트 작성자", "testPassword");
    Article article = Article.create(user.id(), "테스트 제목", "테스트 작성자", "테스트 내용");
    articleDao.save(article);
    request.setPathInfo("/" + article.id() + "/form");
    HttpSession session = request.getSession();
    session.setAttribute("userInfo", user);

    // When
    articleController.doGet(request, response);

    // Then
    assertEquals("/WEB-INF/views/board/article_modify_form.jsp", request.getForwardedPath());
  }

  @Test
  void 게시글_작성자가_아닐시_게시글_수정폼을_요청할_수_없다() throws ServletException, IOException {
    // Given
    User user = User.create("test@test", "테스트 작성자", "testPassword");
    Article article = Article.create(user.id(), "테스트 제목", "테스트 작성자", "테스트 내용");
    articleDao.save(article);

    User otherUser = User.create("test2@test", "테스트 작성자2", "testPassword2");
    request.setPathInfo("/" + article.id() + "/form");
    HttpSession session = request.getSession();
    session.setAttribute("userInfo", otherUser);

    // When
    articleController.doGet(request, response);

    // Then
    assertEquals("/error/403", response.getRedirectLocation());
  }

  @Test
  void 게시글_수정을_할_수_있다() throws ServletException, IOException {
    // Given
    Article article = Article.create("testUserId", "테스트 제목", "테스트 작성자", "테스트 내용");
    articleDao.save(article);
    request.setPathInfo("/" + article.id());
    request.setBody("{\"title\":\"updateTitle\",\"content\":\"updateContent\"}");

    // When
    articleController.doPut(request, response);

    // Then
    Article updateArticle = articleService.findById(article.id());
    assertEquals("updateTitle", updateArticle.title());
    assertEquals("updateContent", updateArticle.content());
  }

  @Test
  void 게시글_작성자가_게시물을_삭제할_수_있다() throws ServletException, IOException {
    // Given
    User user = User.create("test@test", "테스트 작성자", "testPassword");
    Article article = Article.create(user.id(), "테스트 제목", "테스트 작성자", "테스트 내용");
    articleDao.save(article);
    request.setPathInfo("/" + article.id());
    HttpSession session = request.getSession();
    session.setAttribute("userInfo", user);

    // When
    articleController.doDelete(request, response);

    // Then
    assertEquals(HttpServletResponse.SC_OK, response.getStatus());
  }

  @Test
  void 게시글_작성자가_아닐시_삭제할_수_없다() throws ServletException, IOException {
    // Given
    User user = User.create("test@test", "테스트 작성자", "testPassword");
    Article article = Article.create(user.id(), "테스트 제목", "테스트 작성자", "테스트 내용");
    articleDao.save(article);
    request.setPathInfo("/" + article.id());
    HttpSession session = request.getSession();
    User otherUser = User.create("test2@test", "테스트 작성자2", "testPassword2");
    session.setAttribute("userInfo", otherUser);

    // When
    articleController.doDelete(request, response);

    // Then
    assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
  }

  @Test
  void 자신의_게시물에_다른사람의_댓글이_있는경우_삭제할_수_없다() throws ServletException, IOException {
    // Given
    ReplyDao replyDao = new ReplyDao(H2Connector.INSTANCE);
    User user = User.create("test@test", "테스트 작성자", "testPassword");
    Article article = Article.create(user.id(), "테스트 제목", "테스트 작성자", "테스트 내용");
    articleDao.save(article);
    Reply reply = Reply.create(article.id(), "otherUserId", "test2", "test test .");
    replyDao.save(reply);
    request.setPathInfo("/" + article.id());
    HttpSession session = request.getSession();
    session.setAttribute("userInfo", user);

    // When
    articleController.doDelete(request, response);

    // Then
    assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
  }
}
