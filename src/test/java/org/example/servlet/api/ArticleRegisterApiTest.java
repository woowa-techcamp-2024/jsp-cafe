package org.example.servlet.api;

import jakarta.servlet.ServletException;
import org.example.config.DataHandler;
import org.example.domain.Article;
import org.example.mock.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.io.IOException;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArticleRegisterApiTest {

    private static ArticleRegisterApi servlet;
    private static TestArticleDataHandler articleDataHandler;
    private static TestHttpServletRequest request;
    private static TestHttpServletResponse response;
    private static TestServletContext servletContext;
    private static TestServletConfig servletConfig;

    @BeforeAll
    public static void setUp() throws ServletException {
        articleDataHandler = new TestArticleDataHandler();
        request = new TestHttpServletRequest();
        response = new TestHttpServletResponse();
        servletContext = new TestServletContext();

        servletContext.setAttribute(DataHandler.ARTICLE.getValue(), articleDataHandler);
        servletConfig = new TestServletConfig(servletContext);
        servlet = new ArticleRegisterApi();
        servlet.init(servletConfig);
    }

    @Test
    public void testDoPost() throws IOException, ServletException {
        // given
        request.setParameter("title", "Test Title");
        request.setParameter("content", "Test Content");

        // when
        servlet.doPost(request, response);

        // then
        Article article = articleDataHandler.findAll().get(0);
        assertEquals("Test Title", article.getTitle());
        assertEquals("Test Content", article.getContent());
        assertEquals("/articles", response.getRedirectLocation());
    }
}
