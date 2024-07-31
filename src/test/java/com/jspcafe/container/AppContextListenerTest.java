package com.jspcafe.container;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.jspcafe.board.model.ArticleDao;
import com.jspcafe.board.model.ReplyDao;
import com.jspcafe.board.service.ArticleService;
import com.jspcafe.board.service.ReplyService;
import com.jspcafe.test_util.StubServletContext;
import com.jspcafe.user.model.UserDao;
import com.jspcafe.user.service.UserService;
import com.jspcafe.util.MysqlConnector;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppContextListenerTest {

  private AppContextListener listener;
  private ServletContextEvent sce;
  private ServletContext servletContext;

  @BeforeEach
  void setUp() {
    listener = new AppContextListener();
    servletContext = new StubServletContext();
    sce = new ServletContextEvent(servletContext);
  }

  @Test
  void contextInitialized_메서드가_모든_컴포넌트를_초기화한다() {
    // When
    listener.contextInitialized(sce);

    // Then
    assertNotNull(servletContext.getAttribute("databaseConnector"));
    assertInstanceOf(MysqlConnector.class, servletContext.getAttribute("databaseConnector"));

    assertNotNull(servletContext.getAttribute("userDao"));
    assertInstanceOf(UserDao.class, servletContext.getAttribute("userDao"));

    assertNotNull(servletContext.getAttribute("userService"));
    assertInstanceOf(UserService.class, servletContext.getAttribute("userService"));

    assertNotNull(servletContext.getAttribute("articleDao"));
    assertInstanceOf(ArticleDao.class, servletContext.getAttribute("articleDao"));

    assertNotNull(servletContext.getAttribute("articleService"));
    assertInstanceOf(ArticleService.class, servletContext.getAttribute("articleService"));

    assertNotNull(servletContext.getAttribute("replyDao"));
    assertInstanceOf(ReplyDao.class, servletContext.getAttribute("replyDao"));

    assertNotNull(servletContext.getAttribute("replyService"));
    assertInstanceOf(ReplyService.class, servletContext.getAttribute("replyService"));
  }
}
