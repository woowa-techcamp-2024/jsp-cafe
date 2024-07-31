package org.example.servlet;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Field;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginServletTest {

    private LoginServlet loginServlet;
    private UserService userService;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        loginServlet = new LoginServlet();
        userService = mock(UserService.class);

        // Use reflection to inject the mocked UserService
        Field userServiceField = LoginServlet.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(loginServlet, userService);
    }

    @Test
    public void testDoPost_SuccessfulLogin() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getParameter("userId")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getSession()).thenReturn(session);
        when(userService.login("testUser", "password123")).thenReturn(true);

        loginServlet.doPost(request, response);

        verify(session).setAttribute("userId", "testUser");
        verify(response).sendRedirect("/");
    }

    @Test
    public void testDoPost_FailedLogin() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getParameter("userId")).thenReturn("testUser");
        when(request.getParameter("password")).thenReturn("wrongPassword");
        when(request.getSession()).thenReturn(session);
        when(userService.login("testUser", "wrongPassword")).thenReturn(false);

        loginServlet.doPost(request, response);

        verify(response).sendRedirect("/login");
        verify(session, never()).setAttribute(eq("userId"), any());
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/WEB-INF/user/login.jsp")).thenReturn(requestDispatcher);

        loginServlet.doGet(request, response);

        verify(request).getRequestDispatcher("/WEB-INF/user/login.jsp");
        verify(requestDispatcher).forward(request, response);
    }
}
