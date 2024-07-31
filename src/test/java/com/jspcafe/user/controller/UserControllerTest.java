package com.jspcafe.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jspcafe.exception.UserNotFoundException;
import com.jspcafe.test_util.H2Connector;
import com.jspcafe.test_util.H2Initializer;
import com.jspcafe.test_util.StubHttpServletRequest;
import com.jspcafe.test_util.StubHttpServletResponse;
import com.jspcafe.test_util.StubServletConfig;
import com.jspcafe.test_util.StubServletContext;
import com.jspcafe.user.model.User;
import com.jspcafe.user.model.UserDao;
import com.jspcafe.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserControllerTest {

  private UserController userController;
  private StubHttpServletRequest request;
  private StubHttpServletResponse response;
  private StubServletContext servletContext;
  private UserDao userDao;
  private UserService userService;

  @BeforeEach
  void setUp() throws SQLException {
    userController = new UserController();
    request = new StubHttpServletRequest();
    response = new StubHttpServletResponse();
    servletContext = new StubServletContext();

    H2Initializer.initializeDatabase(H2Connector.INSTANCE);
    userDao = new UserDao(H2Connector.INSTANCE);
    userService = new UserService(userDao);
    servletContext.setAttribute("userService", userService);

    userController.init(new StubServletConfig(servletContext));
  }

  @Test
  void 사용자_목록을_조회할_수_있다() throws ServletException, IOException {
    // Given
    List<User> users = Arrays.asList(
        new User("1", "user1@example.com", "사용자1", "password1"),
        new User("2", "user2@example.com", "사용자2", "password2")
    );
    for (User user : users) {
      userDao.save(user);
    }

    request.setPathInfo(null);

    // When
    userController.doGet(request, response);

    // Then
    assertEquals("/WEB-INF/views/user/user_list.jsp", request.getForwardedPath());
    assertEquals(users, request.getAttribute("users"));
  }

  @Test
  void 특정_사용자의_프로필을_조회할_수_있다() throws ServletException, IOException {
    // Given
    User user = new User("1", "user1@example.com", "사용자1", "password1");
    userDao.save(user);

    request.setPathInfo("/1");

    // When
    userController.doGet(request, response);

    // Then
    assertEquals("/WEB-INF/views/user/user_profile.jsp", request.getForwardedPath());
    assertEquals(user, request.getAttribute("user"));
  }

  @Test
  void 회원가입폼을_요청할_경우_회원가입_페이지를_반환한다() throws ServletException, IOException {
    // Given
    request.setPathInfo("/sign");

    // When
    userController.doGet(request, response);

    // Then
    assertEquals("/WEB-INF/views/user/signup.jsp", request.getForwardedPath());
  }

  @Test
  void 새로운_사용자를_등록할_수_있다() throws ServletException, IOException {
    // Given
    request.setParameter("email", "newuser@example.com");
    request.setParameter("nickname", "새사용자");
    request.setParameter("password", "password123");
    request.setPathInfo("/sign");

    // When
    userController.doPost(request, response);

    // Then
    User newUser = userDao.findByEmail("newuser@example.com")
        .orElseThrow(() -> new UserNotFoundException("User email not found"));
    assertNotNull(newUser);
    assertEquals("새사용자", newUser.nickname());
    assertEquals("/users", response.getRedirectLocation());
  }

  @Test
  void 사용자_프로필을_업데이트할_수_있다() throws ServletException, IOException {
    // Given
    User user = User.create("user1@example.com", "사용자1", "password1");
    userDao.save(user);

    request.setPathInfo("/" + user.id());
    request.setBody(
        "{\"currentPassword\":\"password1\",\"email\":\"updated@example.com\",\"nickname\":\"업데이트된사용자\",\"newPassword\":\"newpassword123\"}");

    // When
    userController.doPut(request, response);

    // Then
    User updatedUser = userDao.findById(user.id())
        .orElseThrow(() -> new UserNotFoundException("User id not found"));
    assertEquals("updated@example.com", updatedUser.email());
    assertEquals("업데이트된사용자", updatedUser.nickname());
    assertEquals(HttpServletResponse.SC_SEE_OTHER, response.getStatus());
    assertEquals("/users/" + user.id(), response.getHeader("Location"));
  }

  @Test
  void 사용자가_로그인폼_을_요청할_수_있다() throws ServletException, IOException {
    // Given
    request.setPathInfo("/login");

    // When
    userController.doGet(request, response);

    // Then
    assertEquals("/WEB-INF/views/user/login.jsp", request.getForwardedPath());
  }

  @Test
  void 사용자가_로그인_할_수_있다() throws ServletException, IOException {
    // Given
    User user = User.create("user1@example.com", "사용자1", "password1");
    userDao.save(user);

    request.setPathInfo("/login");
    request.setParameter("email", "user1@example.com");
    request.setParameter("password", "password1");

    // When
    userController.doPost(request, response);

    //Then
    assertEquals(user, request.getSession().getAttribute("userInfo"));
    assertEquals("/", response.getRedirectLocation());
  }

  @Test
  void 잘못된_비밀번호로_로그인할_경우_불일치정보를_request에_넣어준다() throws ServletException, IOException {
    // Given
    User user = User.create("user1@example.com", "사용자1", "password1");
    userDao.save(user);

    request.setPathInfo("/login");
    request.setParameter("email", "user1@example.com");
    request.setParameter("password", "wrongpassword");

    // When
    userController.doPost(request, response);

    //Then
    assertEquals("/WEB-INF/views/user/login.jsp", request.getForwardedPath());
    assertTrue((Boolean) request.getAttribute("loginFailed"));
  }

  @Test
  void 로그아웃시_session을_지우고_루트로_리다이렉트_된다() throws ServletException, IOException {
    // Given
    User user = User.create("user1@example.com", "사용자1", "password1");
    userDao.save(user);

    request.setPathInfo("/login");
    request.setParameter("email", "user1@example.com");
    request.setParameter("password", "password1");

    userController.doPost(request, response);

    // When
    request.setPathInfo("/logout");
    userController.doGet(request, response);

    //Then
    assertNull(request.getSession().getAttribute("userInfo"));
    assertEquals("/", response.getRedirectLocation());
  }

  @Test
  void 프로필_수정페이지를_반환한다() throws ServletException, IOException {
    // Given
    User user = User.create("user1@example.com", "사용자1", "password1");
    userDao.save(user);

    // When
    request.setPathInfo("/" + user.id() + "/form");
    userController.doGet(request, response);

    //Then
    assertEquals(user, request.getAttribute("user"));
    assertEquals("/WEB-INF/views/user/user_update_form.jsp", request.getForwardedPath());
  }

  @Test
  void 실패한_프로필_수정페이지를_반환한다() throws ServletException, IOException {
    // Given
    User user = User.create("user1@example.com", "사용자1", "password1");
    userDao.save(user);

    request.setPathInfo("/" + user.id());
    request.setBody(
        "{\"currentPassword\":\"wrongPassword\",\"email\":\"updated@example.com\",\"nickname\":\"업데이트된사용자\",\"newPassword\":\"newpassword123\"}");

    // When
    userController.doPut(request, response);

    // Then
    assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
    assertEquals("/users/" + user.id() + "/form-failed", response.getHeader("Location"));
  }

  @Test
  void 실패한_프로필_수정페이지를_요청시_반환한다() throws ServletException, IOException {
    // Given
    User user = User.create("user1@example.com", "사용자1", "password1");
    userDao.save(user);

    request.setPathInfo("/" + user.id() + "/form-failed");

    // When
    userController.doGet(request, response);

    // Then
    assertEquals(user, request.getAttribute("user"));
    assertEquals("/WEB-INF/views/user/user_update_form_failed.jsp", request.getForwardedPath());
  }
}
