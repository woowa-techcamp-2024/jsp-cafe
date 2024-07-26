package com.hyeonuk.jspcafe.article.servlet;

import com.hyeonuk.jspcafe.article.dao.ArticleDao;
import com.hyeonuk.jspcafe.article.dao.InMemoryArticleDao;
import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.global.exception.HttpBadRequestException;
import com.hyeonuk.jspcafe.global.exception.HttpNotFoundException;
import com.hyeonuk.jspcafe.member.servlets.mock.BaseHttpServletRequest;
import com.hyeonuk.jspcafe.member.servlets.mock.BaseHttpServletResponse;
import com.hyeonuk.jspcafe.member.servlets.mock.BaseServletConfig;
import com.hyeonuk.jspcafe.member.servlets.mock.MockServletContext;
import jakarta.servlet.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ArticleViewServlet 클래스")
class ArticleViewServletTest {
    private MockRequest req;
    private MockResponse res;
    private ArticleDao articleDao;
    private ArticleViewServlet servlet;

    @BeforeEach
    void setUp() throws ServletException {
        req = new MockRequest();
        res = new MockResponse();
        articleDao = new InMemoryArticleDao();
        servlet = new ArticleViewServlet();
        ServletContext servletContext = new MockServletContext();
        servletContext.setAttribute("articleDao", articleDao);
        ServletConfig servletConfig = new BaseServletConfig(servletContext);
        servlet.init(servletConfig);
    }

    @Nested
    @DisplayName("doGet 메서드")
    class DoGet {
        @Test
        @DisplayName("제대로 된 요청이 들어온 경우, 해당하는 article을 반환한다.")
        void success() throws Exception {
            //given
            Article article1 = new Article(1l, "writer1", "title1", "content1");
            Article article2 = new Article(2l, "writer2", "title2", "content2");
            Article article3 = new Article(3l, "writer3", "title3", "content3");
            Article article4 = new Article(4l, "writer4", "title4", "content4");
            Article article5 = new Article(5l, "writer5", "title5", "content5");
            List<Article> articles = List.of(article1, article2, article3, article4, article5);
            articles.forEach(articleDao::save);
            req.setPathInfo("/" + article1.getId());

            //when
            servlet.doGet(req, res);

            //then
            assertNotNull(req.getAttribute("article"));
            Article article = (Article) req.getAttribute("article");
            assertEquals(article1.getId(), article.getId());
            assertEquals(article1.getTitle(), article.getTitle());
            assertEquals(article1.getWriter(), article.getWriter());
            assertEquals(article1.getContents(), article.getContents());
            assertEquals("/templates/qna/show.jsp", req.getForwardPath());
        }

        @Test
        @DisplayName("pathInfo가 null인 경우")
        void nullPathInfo() throws Exception {
            //given
            Article article1 = new Article(1l, "writer1", "title1", "content1");
            Article article2 = new Article(2l, "writer2", "title2", "content2");
            Article article3 = new Article(3l, "writer3", "title3", "content3");
            Article article4 = new Article(4l, "writer4", "title4", "content4");
            Article article5 = new Article(5l, "writer5", "title5", "content5");
            List<Article> articles = List.of(article1, article2, article3, article4, article5);
            articles.forEach(articleDao::save);
            req.setPathInfo(null);

            //when & then
            assertThrows(HttpBadRequestException.class, () -> {
                servlet.doGet(req, res);
            });
        }

        @Test
        @DisplayName("pathInfo가 empty인 경우")
        void emptyPathInfo() throws Exception {
            //given
            Article article1 = new Article(1l, "writer1", "title1", "content1");
            Article article2 = new Article(2l, "writer2", "title2", "content2");
            Article article3 = new Article(3l, "writer3", "title3", "content3");
            Article article4 = new Article(4l, "writer4", "title4", "content4");
            Article article5 = new Article(5l, "writer5", "title5", "content5");
            List<Article> articles = List.of(article1, article2, article3, article4, article5);
            articles.forEach(articleDao::save);
            req.setPathInfo("");

            //when & then
            assertThrows(HttpBadRequestException.class, () -> {
                servlet.doGet(req, res);
            });
        }

        @Test
        @DisplayName("pathInfo가 /{id}/{anyString}으로 들어온 경우")
        void manyPathVariables() throws Exception {
            //given
            Article article1 = new Article(1l, "writer1", "title1", "content1");
            Article article2 = new Article(2l, "writer2", "title2", "content2");
            Article article3 = new Article(3l, "writer3", "title3", "content3");
            Article article4 = new Article(4l, "writer4", "title4", "content4");
            Article article5 = new Article(5l, "writer5", "title5", "content5");
            List<Article> articles = List.of(article1, article2, article3, article4, article5);
            articles.forEach(articleDao::save);
            req.setPathInfo("/1/2");

            //when & then
            assertThrows(HttpBadRequestException.class, () -> {
                servlet.doGet(req, res);
            });
        }

        @Test
        @DisplayName("pathInfo가 숫자가 아닌 string으로 들어온 경우")
        void pathInfoWithStringId() throws Exception {
            //given
            Article article1 = new Article(1l, "writer1", "title1", "content1");
            Article article2 = new Article(2l, "writer2", "title2", "content2");
            Article article3 = new Article(3l, "writer3", "title3", "content3");
            Article article4 = new Article(4l, "writer4", "title4", "content4");
            Article article5 = new Article(5l, "writer5", "title5", "content5");
            List<Article> articles = List.of(article1, article2, article3, article4, article5);
            articles.forEach(articleDao::save);
            req.setPathInfo("/helloworld");

            //when & then
            assertThrows(HttpBadRequestException.class, () -> {
                servlet.doGet(req, res);
            });
        }

        @Test
        @DisplayName("article이 존재하지 않는 경우")
        void notExistsArticle() throws Exception {
            //given
            Article article1 = new Article(1l, "writer1", "title1", "content1");
            Article article2 = new Article(2l, "writer2", "title2", "content2");
            Article article3 = new Article(3l, "writer3", "title3", "content3");
            Article article4 = new Article(4l, "writer4", "title4", "content4");
            Article article5 = new Article(5l, "writer5", "title5", "content5");
            List<Article> articles = List.of(article1, article2, article3, article4, article5);
            articles.forEach(articleDao::save);
            req.setPathInfo("/6");

            //when & then
            assertThrows(HttpNotFoundException.class, () -> {
                servlet.doGet(req, res);
            });
        }
    }


    private class MockRequest extends BaseHttpServletRequest {
        private Map<String, Object> attributes = new HashMap<>();
        private String forwardPath;
        private String pathInfo;
        private Map<String, String> parameters = new HashMap<>();

        public void setPathInfo(String pathInfo) {
            this.pathInfo = pathInfo;
        }

        @Override
        public String getPathInfo() {
            return this.pathInfo;
        }

        @Override
        public Object getAttribute(String s) {
            return attributes.get(s);
        }

        @Override
        public String getParameter(String s) {
            return parameters.get(s);
        }

        public void setParameter(String key, String value) {
            parameters.put(key, value);
        }

        @Override
        public void setAttribute(String s, Object o) {
            attributes.put(s, o);
        }

        public String getForwardPath() {
            return forwardPath;
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String s) {
            return new RequestDispatcher() {
                @Override
                public void forward(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
                    forwardPath = s;
                }

                @Override
                public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

                }
            };
        }
    }

    private class MockResponse extends BaseHttpServletResponse {
        private String redirection;

        @Override
        public void sendRedirect(String redirection) {
            this.redirection = redirection;
        }

        public String getRedirection() {
            return this.redirection;
        }
    }
}