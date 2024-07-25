package org.example.servlet.api;

import jakarta.servlet.ServletException;
import org.example.mock.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutApiTest {

    private LogoutApi servlet;
    private TestHttpServletRequest request;
    private TestHttpServletResponse response;
    private TestHttpSession session;

    @BeforeEach
    public void setUp() {
        servlet = new LogoutApi();
        request = new TestHttpServletRequest();
        response = new TestHttpServletResponse();
        session = new TestHttpSession();
    }

    @Test
    public void testDoGetWithExistingSession() throws ServletException, IOException {
        // Given
        request.setSession(session);
        session.setAttribute("user", "testUser");

        // When
        servlet.doGet(request, response);

        // Then
        assertEquals("/", response.getRedirectLocation());
    }

    @Test
    public void testDoGetWithoutExistingSession() throws ServletException, IOException {
        // Given
        request.setSession(null);

        // When
        servlet.doGet(request, response);

        // Then
        assertEquals("/", response.getRedirectLocation());
    }
}
