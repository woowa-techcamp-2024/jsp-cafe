package org.example.servlet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LogoutServletTest {

    private LogoutServlet logoutServlet;

    @BeforeEach
    public void setUp() {
        logoutServlet = new LogoutServlet();
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);

        logoutServlet.doPost(request, response);

        // Verify that the "userId" attribute is removed from the session
        verify(session).removeAttribute("userId");

        // Verify that the response redirects to the login page
        verify(response).sendRedirect("/login");
    }
}
