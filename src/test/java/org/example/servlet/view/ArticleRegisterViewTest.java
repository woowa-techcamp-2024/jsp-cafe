package org.example.servlet.view;

import jakarta.servlet.ServletException;
import org.example.domain.User;
import org.example.mock.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

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
        session.setAttribute("user", user);
        request.setSession(session);
        requestDispatcher = new TestRequestDispatcher("/article/register.jsp");
        request.setRequestDispatcher(requestDispatcher);

        // When
        servlet.doGet(request, response);

        // Then
        assertEquals("/article/register.jsp", requestDispatcher.getForwardedPath());
        assertNull(response.getRedirectLocation());
    }

    @Test
    public void testDoGetWithNoSession() throws ServletException, IOException {
        // Given
        request.setSession(null);

        // When
        servlet.doGet(request, response);

        // Then
        assertEquals("/login", response.getRedirectLocation());
        assertNull(requestDispatcher.getForwardedPath());
    }

    @Test
    public void testDoGetWithSessionButNoUser() throws ServletException, IOException {
        // Given
        request.setSession(session);

        // When
        servlet.doGet(request, response);

        // Then
        assertEquals("/login", response.getRedirectLocation());
        assertNull(requestDispatcher.getForwardedPath());
    }
}