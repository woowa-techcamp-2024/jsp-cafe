package org.example.filter;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.util.SessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AuthFilterTest {

    private AuthFilter authFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @Mock
    private HttpSession session;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authFilter = new AuthFilter();
    }

    @Test
    public void testDoFilter_UnauthenticatedUserRedirectsToLogin_ProtectedPath() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/question/123");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(null);
        when(request.getRequestDispatcher("/login")).thenReturn(request.getRequestDispatcher("/login"));

        authFilter.doFilter(request, response, chain);

        verify(request).getRequestDispatcher("/login");
        verify(request.getRequestDispatcher("/login")).forward(request, response);
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    public void testDoFilter_AuthenticatedUserAccessesProtectedPath() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/question/123");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn("testUser");

        User mockUser = new User("testUser", "password", "email@example.com", "nickname");
        when(userRepository.getUserByUserId("testUser")).thenReturn(Optional.of(mockUser));

        // Mock SessionUtil.extractUserId
        try (var mockedSessionUtil = mockStatic(SessionUtil.class)) {
            mockedSessionUtil.when(() -> SessionUtil.extractUserId(request)).thenReturn(Optional.of("testUser"));

            authFilter.doFilter(request, response, chain);
        }

        verify(chain).doFilter(request, response);
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    public void testDoFilter_UnauthenticatedUserAccessesUnprotectedPath() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/public/info");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(null);

        authFilter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    public void testDoFilter_AuthenticatedUserAccessesExactProtectedPath() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/qna/form.html");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn("testUser");

        User mockUser = new User("testUser", "password", "email@example.com", "nickname");
        when(userRepository.getUserByUserId("testUser")).thenReturn(Optional.of(mockUser));

        // Mock SessionUtil.extractUserId
        try (var mockedSessionUtil = mockStatic(SessionUtil.class)) {
            mockedSessionUtil.when(() -> SessionUtil.extractUserId(request)).thenReturn(Optional.of("testUser"));

            authFilter.doFilter(request, response, chain);
        }

        verify(chain).doFilter(request, response);
        verify(request, never()).getRequestDispatcher(anyString());
    }
}
