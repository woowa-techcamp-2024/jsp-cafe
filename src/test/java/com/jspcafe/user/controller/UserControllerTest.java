package com.jspcafe.user.controller;

import com.jspcafe.exception.UserNotFoundException;
import com.jspcafe.test_util.*;
import com.jspcafe.user.model.User;
import com.jspcafe.user.model.UserDao;
import com.jspcafe.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void 새로운_사용자를_등록할_수_있다() throws ServletException, IOException {
        // Given
        request.setParameter("email", "newuser@example.com");
        request.setParameter("nickname", "새사용자");
        request.setParameter("password", "password123");

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
        request.setBody("{\"currentPassword\":\"password1\",\"email\":\"updated@example.com\",\"nickname\":\"업데이트된사용자\",\"newPassword\":\"newpassword123\"}");

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
}
