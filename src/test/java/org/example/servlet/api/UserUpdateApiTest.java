package org.example.servlet.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDateTime;
import org.example.constance.DataHandler;
import org.example.domain.User;
import org.example.mock.TestHttpServletRequest;
import org.example.mock.TestHttpServletResponse;
import org.example.mock.TestRequestDispatcher;
import org.example.mock.TestServletConfig;
import org.example.mock.TestServletContext;
import org.example.mock.TestUserDataHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        request.setAttribute("message", "비밀번호가 맞지 않습니다");
        response.setStatus(400, "BadRequest");

        // 서블릿 메소드 호출
        servlet.doPost(request, response);

        // 검증
        assertEquals(400, response.getStatus());
        assertEquals("비밀번호가 맞지 않습니다", request.getAttribute("message"));
    }
}
