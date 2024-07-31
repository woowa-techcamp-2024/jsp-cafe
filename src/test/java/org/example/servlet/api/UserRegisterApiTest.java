package org.example.servlet.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.servlet.ServletException;
import java.io.IOException;
import org.example.constance.DataHandler;
import org.example.domain.User;
import org.example.mock.TestHttpServletRequest;
import org.example.mock.TestHttpServletResponse;
import org.example.mock.TestServletConfig;
import org.example.mock.TestServletContext;
import org.example.mock.TestUserDataHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


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