package org.example.filter;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StaticResourceFilterTest {

    private StaticResourceFilter filter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        filter = new StaticResourceFilter();
    }

    @Test
    public void testRedirectToUserForm() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/user/form.html");

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect("/static/user/form.html");
        verify(chain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    public void testRedirectToQnaForm() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/qna/form.html");

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect("/static/qna/form.html");
        verify(chain, never()).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    }

    @Test
    public void testRedirectToErrorPage() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/error/not-same-author.html");

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect("/static/error/not-same-author.html");

    }
}