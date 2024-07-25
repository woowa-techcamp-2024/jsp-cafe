package org.example.servlet.api;

import jakarta.servlet.ServletException;
import org.example.config.DataHandler;
import org.example.domain.User;
import org.example.mock.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class UserRegisterApiTest {

    private static UserRegisterApi servlet;
    private static TestUserDataHandler userDataHandler;
    private static TestHttpServletRequest request;
    private static TestHttpServletResponse response;
    private static TestServletContext servletContext;
    private static TestServletConfig servletConfig;

    @BeforeAll
    public static void setUp() throws ServletException {
        servlet = new UserRegisterApi();
        userDataHandler = new TestUserDataHandler();
        request = new TestHttpServletRequest();
        response = new TestHttpServletResponse();
        servletContext = new TestServletContext();
        servletContext.setAttribute(DataHandler.USER.getValue(), userDataHandler);

        servletConfig = new TestServletConfig(servletContext);

        servlet.init(servletConfig);
    }

    @Test
    public void testDoPost() throws IOException, ServletException {
        // 테스트 데이터 설정
        request.setParameter("email", "test@example.com");
        request.setParameter("nickname", "testuser");
        request.setParameter("password", "password123");

        // 서블릿 메소드 호출
        servlet.doPost(request, response);

        // 검증
        User savedUser = userDataHandler.findAll().get(0);
        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("testuser", savedUser.getNickname());
        assertEquals("password123", savedUser.getPassword());
        assertNotNull(savedUser.getCreatedDt());
        assertEquals("/users", response.getRedirectLocation());
    }
}