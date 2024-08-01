package org.example.demo.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.handler.HomeHandler;
import org.example.demo.exception.InternalServerError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class HomeServletTest {

    private HomeServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletContext servletContext;

    @Mock
    private ServletConfig servletConfig;

    @Mock
    private HomeHandler homeHandler;

    @BeforeEach
    void setUp() throws ServletException {
        MockitoAnnotations.openMocks(this);
        servlet = spy(new HomeServlet());
        when(servletConfig.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("homeHandler")).thenReturn(homeHandler);
        doReturn(servletContext).when(servlet).getServletContext();
    }

    @Test
    void HomeServlet_초기화시_라우터에_홈_경로가_등록된다() throws ServletException {
        // Given
        when(servletConfig.getServletContext()).thenReturn(servletContext);

        // When
        servlet.init(servletConfig);

        // Then
        verify(servletContext).getAttribute("homeHandler");
    }

    @Test
    void 루트_경로_요청시_HomeHandler의_handleGetPosts_메서드가_호출된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // When
        servlet.init(servletConfig);  // Ensure servlet is initialized
        servlet.service(request, response);

        // Then
        verify(homeHandler).handleGetPosts(eq(request), eq(response), anyList());
    }

    @Test
    void 존재하지_않는_경로_요청시_404_에러가_발생한다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/non-existent");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");

        // When
        servlet.init(servletConfig);  // Ensure servlet is initialized
        servlet.service(request, response);

        // Then
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void 라우팅_중_IOException_발생시_InternalServerError가_throw된다() throws IOException, ServletException {
        // Given
        when(request.getRequestURI()).thenReturn("/");
        when(request.getContextPath()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");
        doThrow(new IOException("Test IO Exception")).when(homeHandler).handleGetPosts(any(), any(), anyList());

        // When & Then
        servlet.init(servletConfig);  // Ensure servlet is initialized
        assertThrows(InternalServerError.class, () -> servlet.service(request, response));
    }
}
