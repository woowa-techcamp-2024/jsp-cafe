package org.example.servlet.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import jakarta.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDateTime;
import org.example.constance.SessionName;
import org.example.domain.User;
import org.example.mock.TestHttpServletRequest;
import org.example.mock.TestHttpServletResponse;
import org.example.mock.TestHttpSession;
import org.example.mock.TestRequestDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArticleRegisterViewTest {

    private ArticleRegisterView servlet;
    private TestHttpServletRequest request;
    private TestHttpServletResponse response;
    private TestHttpSession session;
    private TestRequestDispatcher requestDispatcher;

    @BeforeEach
    public void setUp() {
        servlet = new ArticleRegisterView();
        request = new TestHttpServletRequest();
        response = new TestHttpServletResponse();
        session = new TestHttpSession();
        requestDispatcher = new TestRequestDispatcher();

        request.setRequestDispatcher(requestDispatcher);
    }

    @Test
    public void testDoGetWithValidSession() throws ServletException, IOException {
        // Given
        User user = new User(1L, "test@example.com", "password", "Test User", LocalDateTime.now());
        session.setAttribute(SessionName.USER.getName(), user);
        request.setSession(session);
        requestDispatcher = new TestRequestDispatcher("/article/register.jsp");
        request.setRequestDispatcher(requestDispatcher);

        // When
        servlet.doGet(request, response);

        // Then
        assertEquals("/article/register.jsp", requestDispatcher.getForwardedPath());
        assertNull(response.getRedirectLocation());
    }
}