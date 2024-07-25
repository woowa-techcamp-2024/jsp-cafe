package org.example.servlet.view;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.DataHandler;
import org.example.domain.Article;
import org.example.mock.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleDetailViewTest {

    private ArticleDetailView servlet;
    private TestArticleDataHandler articleDataHandler;
    private TestHttpServletRequest request;
    private TestHttpServletResponse response;
    private TestServletContext servletContext;
    private TestServletConfig servletConfig;
    private TestRequestDispatcher requestDispatcher;

    @BeforeEach
    public void setUp() throws ServletException {
        articleDataHandler = new TestArticleDataHandler();
        request = new TestHttpServletRequest();
        response = new TestHttpServletResponse();
        servletContext = new TestServletContext();

        servletContext.setAttribute(DataHandler.ARTICLE.getValue(), articleDataHandler);
        servletConfig = new TestServletConfig(servletContext);
        servlet = new ArticleDetailView();
        servlet.init(servletConfig);

        request.setRequestDispatcher(requestDispatcher);
    }

    @Test
    public void testDoGetWithExistingArticle() throws ServletException, IOException {
        // Given
        Long articleId = 1L;
        Article article = new Article(articleId, "Test Title", "Test Content", "Test Author", LocalDateTime.now());
        articleDataHandler.insert(article);
        request.setPathInfo("/" + articleId);
        requestDispatcher = new TestRequestDispatcher("/article/detail.jsp");
        request.setRequestDispatcher(requestDispatcher);

        // When
        servlet.doGet(request, response);
        // Then
        assertEquals("/article/detail.jsp", requestDispatcher.getForwardedPath());
        assertEquals(article, request.getAttribute("article"));
    }

    @Test
    public void testDoGetWithNonExistingArticle() throws ServletException, IOException {
        // Given
        Long articleId = 999L;
        request.setPathInfo("/" + articleId);

        requestDispatcher = new TestRequestDispatcher("/error/error.jsp");
        request.setRequestDispatcher(requestDispatcher);
        request.setAttribute("message", "게시글이 없습니다");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");

        // When
        servlet.doGet(request, response);

        // Then
        assertEquals("/error/error.jsp", requestDispatcher.getForwardedPath());
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
        assertEquals("게시글이 없습니다", request.getAttribute("message"));
    }
}