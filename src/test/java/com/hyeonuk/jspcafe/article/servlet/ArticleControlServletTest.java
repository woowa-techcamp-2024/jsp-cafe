package com.hyeonuk.jspcafe.article.servlet;

import com.hyeonuk.jspcafe.article.dao.ArticleDao;
import com.hyeonuk.jspcafe.article.dao.InMemoryArticleDao;
import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.global.exception.HttpBadRequestException;
import com.hyeonuk.jspcafe.global.exception.HttpNotFoundException;
import com.hyeonuk.jspcafe.member.domain.Member;
import com.hyeonuk.jspcafe.member.servlets.mock.*;
import com.hyeonuk.jspcafe.reply.dao.InMemoryReplyDao;
import com.hyeonuk.jspcafe.reply.dao.ReplyDao;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ArticleViewServlet 클래스")
class ArticleControlServletTest {
    private MockRequest req;
    private MockResponse res;
    private ArticleDao articleDao;
    private ArticleControlServlet servlet;
    private ReplyDao replyDao;

    @BeforeEach
    void setUp() throws ServletException {
        req = new MockRequest();
        res = new MockResponse();
        articleDao = new InMemoryArticleDao();
        replyDao = new InMemoryReplyDao();
        servlet = new ArticleControlServlet();
        ServletContext servletContext = new MockServletContext();
        servletContext.setAttribute("articleDao", articleDao);
        servletContext.setAttribute("replyDao",replyDao);
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
            Article article1 = new Article(1l,new Member(1l,"id1","pw1","nick1","email1"),"title1","contents1");
            Article article2 = new Article(2l,new Member(2l,"id2","pw2","nick2","email2"),"title2","contents2");
            Article article3 = new Article(3l,new Member(3l,"id3","pw3","nick3","email3"),"title3","contents3");
            Article article4 = new Article(4l,new Member(4l,"id4","pw4","nick4","email4"),"title4","contents4");
            Article article5 = new Article(5l,new Member(5l,"id5","pw5","nick5","email5"),"title5","contents5");
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
        @DisplayName("작성자가 자신의 게시글의 /{id}/form 으로 들어온 경우")
        void formPath() throws Exception {
            //given
            Article article1 = new Article(1l,new Member(1l,"id1","pw1","nick1","email1"),"title1","contents1");
            Article article2 = new Article(2l,new Member(2l,"id2","pw2","nick2","email2"),"title2","contents2");
            Article article3 = new Article(3l,new Member(3l,"id3","pw3","nick3","email3"),"title3","contents3");
            Article article4 = new Article(4l,new Member(4l,"id4","pw4","nick4","email4"),"title4","contents4");
            Article article5 = new Article(5l,new Member(5l,"id5","pw5","nick5","email5"),"title5","contents5");
            List<Article> articles = List.of(article1, article2, article3, article4, article5);
            articles.forEach(articleDao::save);
            req.setPathInfo("/1/form");
            MockSession session = new MockSession();
            session.setAttribute("member",new Member(1l,"writer1","pw1","nick1","email1"));
            req.setSession(session);

            //when
            servlet.doGet(req, res);

            //then
            assertNotNull(req.getAttribute("article"));
            Article article = (Article) req.getAttribute("article");
            assertEquals(article1.getId(), article.getId());
            assertEquals(article1.getTitle(), article.getTitle());
            assertEquals(article1.getWriter(), article.getWriter());
            assertEquals(article1.getContents(), article.getContents());
            assertEquals("/templates/qna/modify.jsp", req.getForwardPath());
        }

        @Test
        @DisplayName("작성자가 자신이 아닌 게시글의 /{id}/form 으로 들어온 경우")
        void formPathWithOtherArticle() throws Exception {
            //given
            Article article1 = new Article(1l,new Member(1l,"id1","pw1","nick1","email1"),"title1","contents1");
            Article article2 = new Article(2l,new Member(2l,"id2","pw2","nick2","email2"),"title2","contents2");
            Article article3 = new Article(3l,new Member(3l,"id3","pw3","nick3","email3"),"title3","contents3");
            Article article4 = new Article(4l,new Member(4l,"id4","pw4","nick4","email4"),"title4","contents4");
            Article article5 = new Article(5l,new Member(5l,"id5","pw5","nick5","email5"),"title5","contents5");
            List<Article> articles = List.of(article1, article2, article3, article4, article5);
            articles.forEach(articleDao::save);
            req.setPathInfo("/1/form");
            MockSession session = new MockSession();
            session.setAttribute("member",new Member(2l,"writer2","pw1","nick1","email1"));
            req.setSession(session);

            //when & then
            assertThrows(HttpBadRequestException.class,()->{
                servlet.doGet(req, res);
            });
        }

        @Test
        @DisplayName("pathInfo가 null인 경우")
        void nullPathInfo() throws Exception {
            //given
            Article article1 = new Article(1l,new Member(1l,"id1","pw1","nick1","email1"),"title1","contents1");
            Article article2 = new Article(2l,new Member(2l,"id2","pw2","nick2","email2"),"title2","contents2");
            Article article3 = new Article(3l,new Member(3l,"id3","pw3","nick3","email3"),"title3","contents3");
            Article article4 = new Article(4l,new Member(4l,"id4","pw4","nick4","email4"),"title4","contents4");
            Article article5 = new Article(5l,new Member(5l,"id5","pw5","nick5","email5"),"title5","contents5");
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
            Article article1 = new Article(1l,new Member(1l,"id1","pw1","nick1","email1"),"title1","contents1");
            Article article2 = new Article(2l,new Member(2l,"id2","pw2","nick2","email2"),"title2","contents2");
            Article article3 = new Article(3l,new Member(3l,"id3","pw3","nick3","email3"),"title3","contents3");
            Article article4 = new Article(4l,new Member(4l,"id4","pw4","nick4","email4"),"title4","contents4");
            Article article5 = new Article(5l,new Member(5l,"id5","pw5","nick5","email5"),"title5","contents5");
            List<Article> articles = List.of(article1, article2, article3, article4, article5);
            articles.forEach(articleDao::save);
            req.setPathInfo("");

            //when & then
            assertThrows(HttpBadRequestException.class, () -> {
                servlet.doGet(req, res);
            });
        }

        @Test
        @DisplayName("pathInfo가 /인  경우")
        void slashPathInfo() throws Exception {
            //given
            Article article1 = new Article(1l,new Member(1l,"id1","pw1","nick1","email1"),"title1","contents1");
            Article article2 = new Article(2l,new Member(2l,"id2","pw2","nick2","email2"),"title2","contents2");
            Article article3 = new Article(3l,new Member(3l,"id3","pw3","nick3","email3"),"title3","contents3");
            Article article4 = new Article(4l,new Member(4l,"id4","pw4","nick4","email4"),"title4","contents4");
            Article article5 = new Article(5l,new Member(5l,"id5","pw5","nick5","email5"),"title5","contents5");
            List<Article> articles = List.of(article1, article2, article3, article4, article5);
            articles.forEach(articleDao::save);
            req.setPathInfo("/");

            //when & then
            assertThrows(HttpBadRequestException.class, () -> {
                servlet.doGet(req, res);
            });
        }



        @Test
        @DisplayName("pathInfo가 /{id}/{anyNumber}으로 들어온 경우")
        void manyPathVariables() throws Exception {
            //given
            Article article1 = new Article(1l,new Member(1l,"id1","pw1","nick1","email1"),"title1","contents1");
            Article article2 = new Article(2l,new Member(2l,"id2","pw2","nick2","email2"),"title2","contents2");
            Article article3 = new Article(3l,new Member(3l,"id3","pw3","nick3","email3"),"title3","contents3");
            Article article4 = new Article(4l,new Member(4l,"id4","pw4","nick4","email4"),"title4","contents4");
            Article article5 = new Article(5l,new Member(5l,"id5","pw5","nick5","email5"),"title5","contents5");
            List<Article> articles = List.of(article1, article2, article3, article4, article5);
            articles.forEach(articleDao::save);
            req.setPathInfo("/1/2");

            //when & then
            assertThrows(HttpBadRequestException.class, () -> {
                servlet.doGet(req, res);
            });
        }

        @Test
        @DisplayName("pathInfo가 /{id}/{form 이외의 string}으로 들어온 경우")
        void manyPathVariablesWithString() throws Exception {
            //given
            Article article1 = new Article(1l,new Member(1l,"id1","pw1","nick1","email1"),"title1","contents1");
            Article article2 = new Article(2l,new Member(2l,"id2","pw2","nick2","email2"),"title2","contents2");
            Article article3 = new Article(3l,new Member(3l,"id3","pw3","nick3","email3"),"title3","contents3");
            Article article4 = new Article(4l,new Member(4l,"id4","pw4","nick4","email4"),"title4","contents4");
            Article article5 = new Article(5l,new Member(5l,"id5","pw5","nick5","email5"),"title5","contents5");
            List<Article> articles = List.of(article1, article2, article3, article4, article5);
            articles.forEach(articleDao::save);
            req.setPathInfo("/1/anyString");

            //when & then
            assertThrows(HttpBadRequestException.class, () -> {
                servlet.doGet(req, res);
            });
        }

        @Test
        @DisplayName("pathInfo가 /{id}/{anyString}/{anyString}으로 들어온 경우")
        void manyPathVariablesWithStringDouble() throws Exception {
            //given
            Article article1 = new Article(1l,new Member(1l,"id1","pw1","nick1","email1"),"title1","contents1");
            Article article2 = new Article(2l,new Member(2l,"id2","pw2","nick2","email2"),"title2","contents2");
            Article article3 = new Article(3l,new Member(3l,"id3","pw3","nick3","email3"),"title3","contents3");
            Article article4 = new Article(4l,new Member(4l,"id4","pw4","nick4","email4"),"title4","contents4");
            Article article5 = new Article(5l,new Member(5l,"id5","pw5","nick5","email5"),"title5","contents5");
            List<Article> articles = List.of(article1, article2, article3, article4, article5);
            articles.forEach(articleDao::save);
            req.setPathInfo("/1/anyString/otherString");

            //when & then
            assertThrows(HttpBadRequestException.class, () -> {
                servlet.doGet(req, res);
            });
        }

        @Test
        @DisplayName("pathInfo가 숫자가 아닌 string으로 들어온 경우")
        void pathInfoWithStringId() throws Exception {
            //given
            Article article1 = new Article(1l,new Member(1l,"id1","pw1","nick1","email1"),"title1","contents1");
            Article article2 = new Article(2l,new Member(2l,"id2","pw2","nick2","email2"),"title2","contents2");
            Article article3 = new Article(3l,new Member(3l,"id3","pw3","nick3","email3"),"title3","contents3");
            Article article4 = new Article(4l,new Member(4l,"id4","pw4","nick4","email4"),"title4","contents4");
            Article article5 = new Article(5l,new Member(5l,"id5","pw5","nick5","email5"),"title5","contents5");
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
            Article article1 = new Article(1l,new Member(1l,"id1","pw1","nick1","email1"),"title1","contents1");
            Article article2 = new Article(2l,new Member(2l,"id2","pw2","nick2","email2"),"title2","contents2");
            Article article3 = new Article(3l,new Member(3l,"id3","pw3","nick3","email3"),"title3","contents3");
            Article article4 = new Article(4l,new Member(4l,"id4","pw4","nick4","email4"),"title4","contents4");
            Article article5 = new Article(5l,new Member(5l,"id5","pw5","nick5","email5"),"title5","contents5");
            List<Article> articles = List.of(article1, article2, article3, article4, article5);
            articles.forEach(articleDao::save);
            req.setPathInfo("/6");

            //when & then
            assertThrows(HttpNotFoundException.class, () -> {
                servlet.doGet(req, res);
            });
        }
    }

    @Nested
    @DisplayName("doPost 메서드")
    class DoPost{
        @Nested
        @DisplayName("공통적으로 처리되는 경우")
        class Common{
            @Test
            @DisplayName("pathInfo가 null인 경우")
            void nullPathInfo() throws Exception {
                //given
                req.setPathInfo(null);

                //when & then
                assertThrows(HttpBadRequestException.class, () -> {
                    servlet.doPost(req, res);
                });
            }

            @Test
            @DisplayName("pathInfo가 empty인 경우")
            void emptyPathInfo() throws Exception {
                //given
                req.setPathInfo("");

                //when & then
                assertThrows(HttpBadRequestException.class, () -> {
                    servlet.doPost(req, res);
                });
            }

            @Test
            @DisplayName("pathInfo가 /인  경우")
            void slashPathInfo() throws Exception {
                //given
                req.setPathInfo("/");

                //when & then
                assertThrows(HttpBadRequestException.class, () -> {
                    servlet.doPost(req, res);
                });
            }

            @Test
            @DisplayName("허용된 method value가 아닌 값이 _method에 들어오면 badRequest 오류를 던진다.")
            void badRequestMethodValue() throws Exception {
                //given
                Member member = new Member(1l, "id1", "pw1", "nick1", "email1");
                Article article1 = new Article(1l, member, "title1", "content1");
                articleDao.save(article1);
                req.setPathInfo("/"+ article1.getId());
                req.setParameter("_method","otherMethod");
                MockSession session = new MockSession();
                session.setAttribute("member",member);
                req.setSession(session);

                //when & then
                assertThrows(HttpBadRequestException.class,()->{
                    servlet.doPost(req,res);
                });
            }

            @Test
            @DisplayName("pathInfo가 /{id}/{anyNumber}으로 들어온 경우")
            void manyPathVariables() throws Exception {
                //given
                req.setPathInfo("/1/2");

                //when & then
                assertThrows(HttpBadRequestException.class, () -> {
                    servlet.doPost(req, res);
                });
            }

            @Test
            @DisplayName("pathInfo가 /{id}/{anyString}으로 들어온 경우")
            void manyPathVariablesWithString() throws Exception {
                //given
                Article article1 = new Article(1l,new Member(1l,"id1","pw1","nick1","email1"),"title1","contents1");
                Article article2 = new Article(2l,new Member(2l,"id2","pw2","nick2","email2"),"title2","contents2");
                Article article3 = new Article(3l,new Member(3l,"id3","pw3","nick3","email3"),"title3","contents3");
                Article article4 = new Article(4l,new Member(4l,"id4","pw4","nick4","email4"),"title4","contents4");
                Article article5 = new Article(5l,new Member(5l,"id5","pw5","nick5","email5"),"title5","contents5");
                List<Article> articles = List.of(article1, article2, article3, article4, article5);
                articles.forEach(articleDao::save);
                req.setPathInfo("/1/anyString");

                //when & then
                assertThrows(HttpBadRequestException.class, () -> {
                    servlet.doPost(req, res);
                });
            }

            @Test
            @DisplayName("pathInfo가 숫자가 아닌 string으로 들어온 경우")
            void pathInfoWithStringId() throws Exception {
                //given
                req.setPathInfo("/helloworld");

                //when & then
                assertThrows(HttpBadRequestException.class, () -> {
                    servlet.doPost(req, res);
                });
            }
        }

        @Nested
        @DisplayName("_method가 PUT인 경우")
        class MethodPut{
            @Test
            @DisplayName("작성자가 article을 수정한 경우, 수정된 article이 잘 저장된다.")
            void methodPut() throws Exception {
                //given
                Member member = new Member(1l,"id1","pw1","nick1","email1");
                Article article = new Article(1l,member,"beforeUpdateTitle","beforeUpdateContents");
                String updateTitle = "updatedTitle";
                String updateContents = "updatedContents";
                articleDao.save(article);
                MockSession session = new MockSession();
                session.setAttribute("member",member);
                MockRequest req = new MockRequest();
                MockResponse res = new MockResponse();
                req.setSession(session);
                req.setPathInfo("/"+article.getId());

                req.setParameter("_method","PUT");
                req.setParameter("title",updateTitle);
                req.setParameter("contents",updateContents);

                //when
                servlet.doPost(req,res);

                //then
                Optional<Article> byId = articleDao.findById(article.getId());
                assertTrue(byId.isPresent());
                Article updated = byId.get();
                assertEquals(article.getId(),updated.getId());
                assertEquals(article.getWriter(),updated.getWriter());
                assertEquals(updateTitle,updated.getTitle());
                assertEquals(updateContents,updated.getContents());
                assertEquals("/questions/"+updated.getId(),res.getRedirection());
            }

            @Test
            @DisplayName("작성자가 아닌 사람이 article을 수정한 경우, bad request가 반환된다..")
            void methodPutWithOtherWriter() throws Exception {
                //given
                Member member = new Member(1l,"id1","pw1","nick1","email1");
                Member member2 = new Member(2l,"id2","pw2","nick2","email2");
                Article article = new Article(1l,member,"beforeUpdateTitle","beforeUpdateContents");
                String updateTitle = "updatedTitle";
                String updateContents = "updatedContents";
                articleDao.save(article);
                MockSession session = new MockSession();
                session.setAttribute("member",member2);
                MockRequest req = new MockRequest();
                MockResponse res = new MockResponse();
                req.setSession(session);
                req.setPathInfo("/"+article.getId());

                req.setParameter("_method","PUT");
                req.setParameter("title",updateTitle);
                req.setParameter("contents",updateContents);

                //when & then
                assertThrows(HttpBadRequestException.class,()->{
                    servlet.doPost(req,res);
                });

                //수정이 되면 안된다.
                Optional<Article> byId = articleDao.findById(article.getId());
                assertTrue(byId.isPresent());
                Article notUpdated = byId.get();
                assertEquals(article.getId(),notUpdated.getId());
                assertEquals(article.getWriter(),notUpdated.getWriter());
                assertEquals(article.getTitle(),notUpdated.getTitle());
                assertEquals(article.getContents(),notUpdated.getContents());
            }

            @Test
            @DisplayName("작성자 Null값의 title로 업데이트 한 경우, bad request가 반환된다..")
            void methodPutWithNullTitle() throws Exception {
                //given
                Member member = new Member(1l,"id1","pw1","nick1","email1");
                Article article = new Article(1l,member,"beforeUpdateTitle","beforeUpdateContents");
                String updateTitle = null;
                String updateContents = "updatedContents";
                articleDao.save(article);
                MockSession session = new MockSession();
                session.setAttribute("member",member);
                MockRequest req = new MockRequest();
                MockResponse res = new MockResponse();
                req.setSession(session);
                req.setPathInfo("/"+article.getId());

                req.setParameter("_method","PUT");
                req.setParameter("title",updateTitle);
                req.setParameter("contents",updateContents);

                //when & then
                assertThrows(HttpBadRequestException.class,()->{
                    servlet.doPost(req,res);
                });

                //수정이 되면 안된다.
                Optional<Article> byId = articleDao.findById(article.getId());
                assertTrue(byId.isPresent());
                Article notUpdated = byId.get();
                assertEquals(article.getId(),notUpdated.getId());
                assertEquals(article.getWriter(),notUpdated.getWriter());
                assertEquals(article.getTitle(),notUpdated.getTitle());
                assertEquals(article.getContents(),notUpdated.getContents());
            }

            @Test
            @DisplayName("작성자 blank값의 title로 업데이트 한 경우, bad request가 반환된다..")
            void methodPutWithBlankTitle() throws Exception {
                //given
                Member member = new Member(1l,"id1","pw1","nick1","email1");
                Article article = new Article(1l,member,"beforeUpdateTitle","beforeUpdateContents");
                String updateTitle = "    ";
                String updateContents = "updatedContents";
                articleDao.save(article);
                MockSession session = new MockSession();
                session.setAttribute("member",member);
                MockRequest req = new MockRequest();
                MockResponse res = new MockResponse();
                req.setSession(session);
                req.setPathInfo("/"+article.getId());

                req.setParameter("_method","PUT");
                req.setParameter("title",updateTitle);
                req.setParameter("contents",updateContents);

                //when & then
                assertThrows(HttpBadRequestException.class,()->{
                    servlet.doPost(req,res);
                });

                //수정이 되면 안된다.
                Optional<Article> byId = articleDao.findById(article.getId());
                assertTrue(byId.isPresent());
                Article notUpdated = byId.get();
                assertEquals(article.getId(),notUpdated.getId());
                assertEquals(article.getWriter(),notUpdated.getWriter());
                assertEquals(article.getTitle(),notUpdated.getTitle());
                assertEquals(article.getContents(),notUpdated.getContents());
            }

            @Test
            @DisplayName("작성자 Null값의 contents로 업데이트 한 경우, bad request가 반환된다..")
            void methodPutWithNullContents() throws Exception {
                //given
                Member member = new Member(1l,"id1","pw1","nick1","email1");
                Article article = new Article(1l,member,"beforeUpdateTitle","beforeUpdateContents");
                String updateTitle = "updatedTitle";
                String updateContents = null;
                articleDao.save(article);
                MockSession session = new MockSession();
                session.setAttribute("member",member);
                MockRequest req = new MockRequest();
                MockResponse res = new MockResponse();
                req.setSession(session);
                req.setPathInfo("/"+article.getId());

                req.setParameter("_method","PUT");
                req.setParameter("title",updateTitle);
                req.setParameter("contents",updateContents);

                //when & then
                assertThrows(HttpBadRequestException.class,()->{
                    servlet.doPost(req,res);
                });

                //수정이 되면 안된다.
                Optional<Article> byId = articleDao.findById(article.getId());
                assertTrue(byId.isPresent());
                Article notUpdated = byId.get();
                assertEquals(article.getId(),notUpdated.getId());
                assertEquals(article.getWriter(),notUpdated.getWriter());
                assertEquals(article.getTitle(),notUpdated.getTitle());
                assertEquals(article.getContents(),notUpdated.getContents());
            }

            @Test
            @DisplayName("작성자 blank값의 contents로 업데이트 한 경우, bad request가 반환된다..")
            void methodPutWithBlankContents() throws Exception {
                //given
                Member member = new Member(1l,"id1","pw1","nick1","email1");
                Article article = new Article(1l,member,"beforeUpdateTitle","beforeUpdateContents");
                String updateTitle = "updatedTitle";
                String updateContents = "    ";
                articleDao.save(article);
                MockSession session = new MockSession();
                session.setAttribute("member",member);
                MockRequest req = new MockRequest();
                MockResponse res = new MockResponse();
                req.setSession(session);
                req.setPathInfo("/"+article.getId());

                req.setParameter("_method","PUT");
                req.setParameter("title",updateTitle);
                req.setParameter("contents",updateContents);

                //when & then
                assertThrows(HttpBadRequestException.class,()->{
                    servlet.doPost(req,res);
                });

                //수정이 되면 안된다.
                Optional<Article> byId = articleDao.findById(article.getId());
                assertTrue(byId.isPresent());
                Article notUpdated = byId.get();
                assertEquals(article.getId(),notUpdated.getId());
                assertEquals(article.getWriter(),notUpdated.getWriter());
                assertEquals(article.getTitle(),notUpdated.getTitle());
                assertEquals(article.getContents(),notUpdated.getContents());
            }
        }

        @Nested
        @DisplayName("_method가 DELETE인 경우")
        class MethodDelete {
            @DisplayName("작성자가 자신의 게시글을 삭제하면 잘 삭제된다.")
            @Test
            void deleteSuccessTest() throws Exception{
                //given
                Member member = new Member(1l,"id1","pw1","nick1","email1");
                Article article = new Article(1l,member,"title","contents");
                articleDao.save(article);
                MockSession session = new MockSession();
                session.setAttribute("member",member);
                MockRequest req = new MockRequest();
                MockResponse res = new MockResponse();
                req.setSession(session);
                req.setPathInfo("/"+article.getId());

                req.setParameter("_method","DELETE");

                //when
                servlet.doPost(req,res);

                //then
                assertTrue(articleDao.findById(article.getId()).isEmpty());
                assertEquals("/",res.getRedirection());
            }

            @Test
            @DisplayName("작성자가 아닌 사람이 삭제요청을 하면 삭제가 안되고 BadRequest 오류를 던진다.")
            void deleteWithOtherWriter() throws Exception{
                //given
                Member member = new Member(1l,"id1","pw1","nick1","email1");
                Member member2 = new Member(2l,"id2","pw2","nick2","email2");
                Article article = new Article(1l,member,"title","contents");
                articleDao.save(article);
                MockSession session = new MockSession();
                session.setAttribute("member",member2);
                MockRequest req = new MockRequest();
                MockResponse res = new MockResponse();
                req.setSession(session);
                req.setPathInfo("/"+article.getId());

                req.setParameter("_method","DELETE");

                //when & then
                assertThrows(HttpBadRequestException.class,()->{
                    servlet.doPost(req,res);
                });

                //수정이 되면 안된다.
                Optional<Article> byId = articleDao.findById(article.getId());
                assertTrue(byId.isPresent());
                Article notDeleted = byId.get();
                assertEquals(article.getId(),notDeleted.getId());
                assertEquals(article.getWriter(),notDeleted.getWriter());
                assertEquals(article.getTitle(),notDeleted.getTitle());
                assertEquals(article.getContents(),notDeleted.getContents());
            }
        }
    }

    private class MockRequest extends BaseHttpServletRequest {
        private Map<String, Object> attributes = new HashMap<>();
        private String forwardPath;
        private String pathInfo;
        private Map<String, String> parameters = new HashMap<>();
        private MockSession session;

        public void setSession(MockSession session){
            this.session = session;
        }
        @Override
        public HttpSession getSession() {
            return session;
        }

        @Override
        public HttpSession getSession(boolean b) {
            return session;
        }

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