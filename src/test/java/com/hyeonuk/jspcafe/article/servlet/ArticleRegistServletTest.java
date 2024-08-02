package com.hyeonuk.jspcafe.article.servlet;

import com.hyeonuk.jspcafe.article.dao.ArticleDao;
import com.hyeonuk.jspcafe.article.dao.InMemoryArticleDao;
import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.global.exception.InvalidArticleRegistRequest;
import com.hyeonuk.jspcafe.member.domain.Member;
import com.hyeonuk.jspcafe.member.servlets.mock.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ArticleRegistServlet 클래스")
class ArticleRegistServletTest {
    private MockRequest req;
    private MockResponse res;
    private ArticleDao articleDao;
    private ArticleRegistServlet servlet;
    private Member member;
    @BeforeEach
    void setUp() throws ServletException {
        req = new MockRequest();
        res = new MockResponse();
        articleDao = new InMemoryArticleDao();
        servlet = new ArticleRegistServlet();
        member = new Member(1l,"member1","password1","nickname1","email1");
        ServletContext servletContext = new MockServletContext();
        servletContext.setAttribute("articleDao", articleDao);
        ServletConfig servletConfig = new BaseServletConfig(servletContext);
        servlet.init(servletConfig);
    }

    @Nested
    @DisplayName("doGet 메서드는")
    class DoGet {
        @DisplayName("접근하면 /templates/qna/form.jsp로 포워딩한다.")
        @Test
        void forwardingToForm() throws Exception{
            //given

            //when
            servlet.doGet(req,res);

            //then
            assertEquals("/templates/qna/form.jsp",req.getForwardPath());
        }
    }

    @Nested
    @DisplayName("doPost 메서드는")
    class DoPost {
        @DisplayName("정상적으로 등록이 되면 / 로 리다이렉트 합니다.")
        @Test
        void success() throws Exception{
            //given
            String title = "title1";
            String contents = "contents1";
            req.setParameter("title", title);
            req.setParameter("contents", contents);
            HttpSession session = new MockSession();
            session.setAttribute("member",member);
            req.setSession(session);

            //when
            servlet.doPost(req,res);

            //then
            assertEquals(1,articleDao.findAll().size());
            Article saved = articleDao.findAll().get(0);
            assertEquals(saved.getWriter().getId(),member.getId());
            assertEquals(saved.getTitle(),title);
            assertEquals(saved.getContents(),contents);
            assertEquals("/",res.getRedirection());
        }

        @DisplayName("세션이 null이면 저장되지 않고 /login으로 redirect한다.")
        @Test
        void withoutSession() throws Exception{
            //given
            String title = "title1";
            String contents = "contents1";
            req.setParameter("title", title);
            req.setParameter("contents", contents);

            //when
            servlet.doPost(req,res);

            //then
            assertEquals(0,articleDao.findAll().size());
            assertEquals("/login",res.getRedirection());
        }

        @DisplayName("세션에 member 객체가 없다면 저장되지 않고 /login으로 redirect한다.")
        @Test
        void withoutSessionValue() throws Exception{
            //given
            String title = "title1";
            String contents = "contents1";
            req.setParameter("title", title);
            req.setParameter("contents", contents);
            HttpSession session = new MockSession();
            req.setSession(session);

            //when
            servlet.doPost(req,res);

            //then
            assertEquals(0,articleDao.findAll().size());
            assertEquals("/login",res.getRedirection());
        }

        @Nested
        @DisplayName("article의 유효성 검사")
        class ArticleValidation{
            @DisplayName("title이 null이면 저장되지 않고 오류를 반환한다.")
            @Test
            void nullTitle() throws Exception{
                //given
                String title = null;
                String contents = "contents1";
                req.setParameter("title", title);
                req.setParameter("contents", contents);
                HttpSession session = new MockSession();
                session.setAttribute("member",member);
                req.setSession(session);

                //when & then
                assertThrows(InvalidArticleRegistRequest.class,()->{
                    servlet.doPost(req,res);
                });
                assertEquals(0,articleDao.findAll().size());
            }
            @DisplayName("title이 blank면 저장되지 않고 오류를 반환한다.")
            @Test
            void blankTitle() throws Exception{
                //given
                String title = "  ";
                String contents = "contents1";
                req.setParameter("title", title);
                req.setParameter("contents", contents);
                HttpSession session = new MockSession();
                session.setAttribute("member",member);
                req.setSession(session);

                //when & then
                assertThrows(InvalidArticleRegistRequest.class,()->{
                    servlet.doPost(req,res);
                });
                assertEquals(0,articleDao.findAll().size());
            }

            @DisplayName("contents가 null이면 저장되지 않고 오류를 반환한다.")
            @Test
            void nullContents() throws Exception{
                //given
                String title = "title1";
                String contents = null;
                req.setParameter("title", title);
                req.setParameter("contents", contents);
                HttpSession session = new MockSession();
                session.setAttribute("member",member);
                req.setSession(session);

                //when & then
                assertThrows(InvalidArticleRegistRequest.class,()->{
                    servlet.doPost(req,res);
                });
                assertEquals(0,articleDao.findAll().size());
            }

            @DisplayName("contents가 blank면 저장되지 않고 오류를 반환한다.")
            @Test
            void blankContents() throws Exception{
                //given
                String title = "title1";
                String contents = "   ";
                req.setParameter("title", title);
                req.setParameter("contents", contents);
                HttpSession session = new MockSession();
                session.setAttribute("member",member);
                req.setSession(session);

                //when & then
                assertThrows(InvalidArticleRegistRequest.class,()->{
                    servlet.doPost(req,res);
                });
                assertEquals(0,articleDao.findAll().size());
            }
        }
    }

    private class MockRequest extends BaseHttpServletRequest {
        private Map<String,Object> attributes = new HashMap<>();
        private String forwardPath;
        private String pathInfo;
        private Map<String,String> parameters = new HashMap<>();
        private HttpSession session;
        public void setSession(HttpSession session){
            this.session = session;
        }
        @Override
        public HttpSession getSession(){
            return session;
        }

        @Override
        public HttpSession getSession(boolean create) {
            if(create && session == null){
                return new MockSession();
            }
            return session;
        }

        @Override
        public Object getAttribute(String s) {
            return attributes.get(s);
        }

        @Override
        public String getParameter(String s) {
            return parameters.get(s);
        }
        public void setParameter(String key,String value){
            parameters.put(key,value);
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
        private String redirection;
        @Override
        public void sendRedirect(String redirection){
            this.redirection = redirection;
        }
        public String getRedirection(){
            return this.redirection;
        }
    }
}