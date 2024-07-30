package com.hyeonuk.jspcafe.article.servlet;

import com.hyeonuk.jspcafe.article.dao.ArticleDao;
import com.hyeonuk.jspcafe.article.dao.InMemoryArticleDao;
import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.member.domain.Member;
import com.hyeonuk.jspcafe.member.servlets.mock.*;
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

@DisplayName("ArticleListServlet 클래스")
class ArticleListServletTest {
    private MockRequest req;
    private MockResponse res;
    private ArticleDao articleDao;
    private ArticleListServlet servlet;
    @BeforeEach
    void setUp() throws ServletException {
        req = new MockRequest();
        res = new MockResponse();
        articleDao = new InMemoryArticleDao();
        servlet = new ArticleListServlet();
        ServletContext servletContext = new MockServletContext();
        servletContext.setAttribute("articleDao", articleDao);
        ServletConfig servletConfig = new BaseServletConfig(servletContext);
        servlet.init(servletConfig);
    }

    @Nested
    @DisplayName("doGet 메서드")
    class DoGet {
        @DisplayName("모든 article을 조회하고 attribute에 저장한 뒤 qnaList.jsp로 포워딩한다.")
        @Test
        void forwardWithAllArticles() throws ServletException, IOException {
            //given
            Article article1 = new Article(1l,new Member(1l,"id1","pw1","nick1","email1"),"title1","contents1");
            Article article2 = new Article(2l,new Member(2l,"id2","pw2","nick2","email2"),"title2","contents2");
            Article article3 = new Article(3l,new Member(3l,"id3","pw3","nick3","email3"),"title3","contents3");
            Article article4 = new Article(4l,new Member(4l,"id4","pw4","nick4","email4"),"title4","contents4");
            Article article5 = new Article(5l,new Member(5l,"id5","pw5","nick5","email5"),"title5","contents5");
            List<Article> articles = List.of(article1, article2, article3, article4, article5);
            articles.forEach(articleDao::save);

            //when
            servlet.doGet(req,res);

            //then
            assertNotNull(req.getAttribute("articles"));
            assertTrue(req.getAttribute("articles") instanceof List);
            List<Article> list = (List<Article>)req.getAttribute("articles");
            boolean allMatch = list.stream().allMatch(article -> articles.stream().anyMatch(a -> compareArticle(a, article)));
            assertTrue(allMatch);
            assertEquals("/qnaList.jsp",req.getForwardPath());
        }

        private boolean compareArticle(Article expected, Article actual){
            return (expected.getTitle() != null && expected.getTitle().equals(actual.getTitle()))
                    && (expected.getContents() != null && expected.getContents().equals(actual.getContents()))
                    && (expected.getId() != null && expected.getId().equals(actual.getId()))
                    && (expected.getWriter() != null && expected.getWriter().equals(actual.getWriter()));
        }
    }

    private class MockRequest extends BaseHttpServletRequest{
        private Map<String,Object> attributes = new HashMap<>();
        private String forwardPath;
        @Override
        public Object getAttribute(String s) {
            return attributes.get(s);
        }

        @Override
        public void setAttribute(String s, Object o) {
            attributes.put(s,o);
        }

        public String getForwardPath(){
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

    }
}