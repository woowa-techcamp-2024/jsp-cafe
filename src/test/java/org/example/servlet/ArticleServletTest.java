package org.example.servlet;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import org.example.dto.ArticleCreateReqDto;
import org.example.service.ArticleService;
import org.example.util.SessionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ArticleServletTest {

    private ArticleServlet articleServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ArticleService articleService;

    @BeforeEach
    public void setUp() throws IllegalAccessException, NoSuchFieldException {
        MockitoAnnotations.openMocks(this);
        articleServlet = new ArticleServlet();
        articleService = mock(ArticleService.class);

        // Use reflection to inject the mocked ArticleService
        Field articleServiceField = ArticleServlet.class.getDeclaredField("articleService");
        articleServiceField.setAccessible(true);
        articleServiceField.set(articleServlet, articleService);
    }

    @Test
    public void testDoPost_AuthorExists() throws IOException, ServletException {
        when(request.getParameter("title")).thenReturn("Test Title");
        when(request.getParameter("content")).thenReturn("Test Content");
        try (var mockedSessionUtil = mockStatic(SessionUtil.class)) {
            mockedSessionUtil.when(() -> SessionUtil.extractUserId(request)).thenReturn(Optional.of("testUser"));

            articleServlet.doPost(request, response);

            verify(articleService).save(new ArticleCreateReqDto("testUser", "Test Title", "Test Content"));
            verify(response).sendRedirect("/");
        }
    }

    @Test
    public void testDoPost_AuthorDoesNotExist() throws IOException, ServletException {
        when(request.getParameter("title")).thenReturn("Test Title");
        when(request.getParameter("content")).thenReturn("Test Content");
        try (var mockedSessionUtil = mockStatic(SessionUtil.class)) {
            mockedSessionUtil.when(() -> SessionUtil.extractUserId(request)).thenReturn(Optional.empty());

            articleServlet.doPost(request, response);

            verify(articleService, never()).save(any(ArticleCreateReqDto.class));
            verify(response).sendRedirect("/login");
        }
    }
}
