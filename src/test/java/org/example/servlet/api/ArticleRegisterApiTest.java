package org.example.servlet.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.servlet.ServletException;
import java.io.IOException;
import java.time.LocalDateTime;
import org.example.constance.DataHandler;
import org.example.constance.SessionName;
import org.example.domain.Article;
import org.example.domain.User;
import org.example.mock.TestArticleDataHandler;
import org.example.mock.TestHttpServletRequest;
import org.example.mock.TestHttpServletResponse;
import org.example.mock.TestHttpSession;
import org.example.mock.TestServletConfig;
import org.example.mock.TestServletContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ArticleRegisterApiTest {

    private static ArticleRegisterApi servlet;
    private static TestArticleDataHandler articleDataHandler;
    private static TestHttpServletRequest request;
    private static TestHttpServletResponse response;
    private static TestHttpSession session;
    private static TestServletContext servletContext;
    private static TestServletConfig servletConfig;


    @BeforeAll
    public static void setUp() throws ServletException {
        articleDataHandler = new TestArticleDataHandler();
        request = new TestHttpServletRequest();
        response = new TestHttpServletResponse();
        session = new TestHttpSession();
        servletContext = new TestServletContext();

        servletContext.setAttribute(DataHandler.ARTICLE.getValue(), articleDataHandler);
        servletConfig = new TestServletConfig(servletContext);
        servlet = new ArticleRegisterApi();
        servlet.init(servletConfig);

        User user = new User(1L, "a@a.com", "b", "c", LocalDateTime.now());
        session.setAttribute(SessionName.USER.getName(), user);
        request.setSession(session);
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
        assertEquals("b", article.getAuthor());
        assertEquals("/", response.getRedirectLocation());
    }
}
