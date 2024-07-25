package org.example.servlet.api;

import jakarta.servlet.ServletException;
import org.example.config.DataHandler;
import org.example.domain.User;
import org.example.mock.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserUpdateApiTest {
    private static UserUpdateApi servlet;
    private static TestUserDataHandler userDataHandler;
    private static TestHttpServletRequest request;
    private static TestHttpServletResponse response;
    private static TestServletContext servletContext;
    private static TestServletConfig servletConfig;

    @BeforeAll
    public static void setUp() throws ServletException {
        servlet = new UserUpdateApi();
        userDataHandler = new TestUserDataHandler();
        request = new TestHttpServletRequest();
        response = new TestHttpServletResponse();
        servletContext = new TestServletContext();
        servletContext.setAttribute(DataHandler.USER.getValue(), userDataHandler);

        servletConfig = new TestServletConfig(servletContext);

        servlet.init(servletConfig);

        // 테스트용 사용자 추가
        User testUser = new User(1L, "old@example.com", "olduser", "oldpassword", LocalDateTime.now());
        userDataHandler.update(testUser);
    }

    @Test
    public void testDoPostSuccessfulUpdate() throws IOException, ServletException {
        // 테스트 데이터 설정
        request.setParameter("userId", "1");
        request.setParameter("email", "new@example.com");
        request.setParameter("nickname", "newuser");
        request.setParameter("password", "oldpassword");

        // 서블릿 메소드 호출
        servlet.doPost(request, response);

        // 검증
        User updatedUser = userDataHandler.findByUserId(1L);
        assertNotNull(updatedUser);
        assertEquals("new@example.com", updatedUser.getEmail());
        assertEquals("newuser", updatedUser.getNickname());
        assertEquals("oldpassword", updatedUser.getPassword());
        assertEquals("/users/1", response.getRedirectLocation());
    }

    @Test
    public void testDoPostWrongPassword() throws IOException, ServletException {
        // 테스트 데이터 설정
        request.setParameter("userId", "1");
        request.setParameter("email", "new@example.com");
        request.setParameter("nickname", "newuser");
        request.setParameter("password", "wrongpassword");

        TestRequestDispatcher dispatcher = new TestRequestDispatcher();
        request.setRequestDispatcher(dispatcher);
        request.setAttribute("message", "기존 비밀번호와 맞지 않습니다");
        response.setStatus(400, "BadRequest");

        // 서블릿 메소드 호출
        servlet.doPost(request, response);

        // 검증
        assertEquals(400, response.getStatus());
        assertEquals("기존 비밀번호와 맞지 않습니다", request.getAttribute("message"));
    }
}
