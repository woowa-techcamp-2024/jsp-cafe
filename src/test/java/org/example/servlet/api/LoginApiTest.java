package org.example.servlet.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.DataHandler;
import org.example.domain.User;
import org.example.mock.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class LoginApiTest {

    private LoginApi servlet;
    private TestUserDataHandler userDataHandler;
    private TestHttpServletRequest request;
    private TestHttpServletResponse response;
    private TestServletContext servletContext;
    private TestServletConfig servletConfig;
    private TestHttpSession session;

    @BeforeEach
    public void setUp() throws ServletException {
        userDataHandler = new TestUserDataHandler();
        request = new TestHttpServletRequest();
        response = new TestHttpServletResponse();
        servletContext = new TestServletContext();
        session = new TestHttpSession();

        servletContext.setAttribute(DataHandler.USER.getValue(), userDataHandler);
        servletConfig = new TestServletConfig(servletContext);
        servlet = new LoginApi();
        servlet.init(servletConfig);
    }

    @Test
    public void testSuccessfulLogin() throws IOException, ServletException {
        // given
        User user = new User("test@example.com", "c", "b", LocalDateTime.now());
        userDataHandler.insert(user);
        request.setParameter("email", "test@example.com");
        request.setParameter("password", "b");

        TestRequestDispatcher dispatcher = new TestRequestDispatcher();
        request.setRequestDispatcher(dispatcher);

        // when
        servlet.doPost(request, response);

        // then
        assertEquals("/", response.getRedirectLocation());
    }

    @Test
    public void testUserNotFound() throws IOException, ServletException {
        // given
        request.setParameter("email", "nonexistent@example.com");
        request.setParameter("password", "password");

        TestRequestDispatcher dispatcher = new TestRequestDispatcher();
        request.setRequestDispatcher(dispatcher);
        request.setAttribute("message", "User 가 없습니다.");
        response.setStatus(HttpServletResponse.SC_NOT_FOUND, "Not Found");
        // when
        servlet.doPost(request, response);

        // then
        assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
        assertEquals("User 가 없습니다.", request.getAttribute("message"));
    }

    @Test
    public void testInvalidPassword() throws IOException, ServletException {
        // given
        User user = new User("test@example.com",  "Test User", "password",LocalDateTime.now());
        userDataHandler.insert(user);
        request.setParameter("email", "test@example.com");
        request.setParameter("password", "wrongpassword");

        TestRequestDispatcher dispatcher = new TestRequestDispatcher();
        request.setRequestDispatcher(dispatcher);
        request.setAttribute("message", "비밀번호가 맞지 않습니다");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
        // when
        servlet.doPost(request, response);

        // then
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        assertEquals("비밀번호가 맞지 않습니다", request.getAttribute("message"));
    }
}
