package org.example.demo.router;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.HttpMethod;
import org.example.demo.exception.InternalServerError;
import org.example.demo.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RouterTest {

    private Router router;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        router = new Router();
    }

    @Test
    void 라우트_등록_후_매칭되는_요청이_오면_핸들러가_실행된다() throws IOException, ServletException {
        // Given
        RouteHandler handler = mock(RouteHandler.class);
        router.addRoute(HttpMethod.GET, "/test/(\\d+)", handler);
        when(request.getRequestURI()).thenReturn("/test/123");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // When
        boolean result = router.route(request, response);

        // Then
        assertTrue(result);
        verify(handler).handle(eq(request), eq(response), eq(List.of("123")));
    }

    @Test
    void 등록되지_않은_경로로_요청이_오면_라우팅에_실패한다() {
        // Given
        when(request.getRequestURI()).thenReturn("/nonexistent");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // When
        boolean result = router.route(request, response);

        // Then
        assertFalse(result);
    }

    @Test
    void 등록된_경로와_일치하지만_메소드가_다르면_라우팅에_실패한다() {
        // Given
        RouteHandler handler = mock(RouteHandler.class);
        router.addRoute(HttpMethod.GET, "/test", handler);
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");

        // When
        boolean result = router.route(request, response);

        // Then
        assertFalse(result);
        verifyNoInteractions(handler);
    }

    @Test
    void hidden_메소드_파라미터를_통해_HTTP_메소드를_오버라이드할_수_있다() throws IOException, ServletException {
        // Given
        RouteHandler handler = mock(RouteHandler.class);
        router.addRoute(HttpMethod.PUT, "/test", handler);
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("_method")).thenReturn("PUT");

        // When
        boolean result = router.route(request, response);

        // Then
        assertTrue(result);
        verify(handler).handle(eq(request), eq(response), eq(List.of()));
    }

    @Test
    void 핸들러에서_UnauthorizedException이_발생하면_403_상태코드를_반환한다() throws IOException, ServletException {
        // Given
        RouteHandler handler = mock(RouteHandler.class);
        doThrow(new UnauthorizedException("Unauthorized")).when(handler).handle(any(), any(), any());
        router.addRoute(HttpMethod.GET, "/test", handler);
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // When
        boolean result = router.route(request, response);

        // Then
        assertTrue(result);
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    void 핸들러에서_IOException이_발생하면_InternalServerError가_발생한다() throws IOException, ServletException {
        // Given
        RouteHandler handler = mock(RouteHandler.class);
        doThrow(new IOException("IO Error")).when(handler).handle(any(), any(), any());
        router.addRoute(HttpMethod.GET, "/test", handler);
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // When & Then
        assertThrows(InternalServerError.class, () -> router.route(request, response));
    }
}