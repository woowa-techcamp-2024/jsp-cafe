package org.example.servlet.view;


import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import org.example.constance.AliveStatus;
import org.example.constance.DataHandler;
import org.example.domain.Article;
import org.example.mock.TestArticleDataHandler;
import org.example.mock.TestHttpServletRequest;
import org.example.mock.TestHttpServletResponse;
import org.example.mock.TestRequestDispatcher;
import org.example.mock.TestServletConfig;
import org.example.mock.TestServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Article article = new Article(articleId, "Test Title", "Test Content", "Test Author", LocalDateTime.now(),
                AliveStatus.ALIVE, 1L);
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