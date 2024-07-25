package org.example.servlet.view;

import jakarta.servlet.ServletException;
import org.example.config.DataHandler;
import org.example.domain.Article;
import org.example.mock.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArticleListViewTest {
    private ArticlesListView servlet;
    private TestArticleDataHandler articleDataHandler;
    private TestHttpServletRequest request;
    private TestHttpServletResponse response;
    private TestServletContext servletContext;
    private TestServletConfig servletConfig;
    private TestRequestDispatcher requestDispatcher;

    @BeforeEach
    public void setUp() throws ServletException {
        servlet = new ArticlesListView();
        articleDataHandler = new TestArticleDataHandler();
        request = new TestHttpServletRequest();
        response = new TestHttpServletResponse();
        servletContext = new TestServletContext();
        requestDispatcher = new TestRequestDispatcher("/article/list.jsp");

        servletContext.setAttribute(DataHandler.ARTICLE.getValue(), articleDataHandler);
        servletConfig = new TestServletConfig(servletContext);
        servlet.init(servletConfig);

        request.setRequestDispatcher(requestDispatcher);
    }

    @Test
    public void testDoGetWithArticles() throws ServletException, IOException {
        // Given
        List<Article> articles = Arrays.asList(
                new Article(1L, "Title 1", "Content 1", "Author 1", LocalDateTime.now()),
                new Article(2L, "Title 2", "Content 2", "Author 2", LocalDateTime.now().minusDays(1))
        );
        for(Article article : articles){
            articleDataHandler.insert(article);
        }

        // When
        servlet.doGet(request, response);

        // Then
        assertEquals("/article/list.jsp", requestDispatcher.getForwardedPath());
        assertNotNull(request.getAttribute("articles"));
        assertEquals(articles, request.getAttribute("articles"));
    }
}