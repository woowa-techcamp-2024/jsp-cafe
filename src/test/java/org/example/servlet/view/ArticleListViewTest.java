package org.example.servlet.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
                new Article(1L, "Title 1", "Content 1", "Author 1", LocalDateTime.now(), AliveStatus.ALIVE, 1L),
                new Article(2L, "Title 2", "Content 2", "Author 2", LocalDateTime.now().minusDays(1), AliveStatus.ALIVE,
                        2L)
        );
        for (Article article : articles) {
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