package com.hyeonuk.jspcafe.reply.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyeonuk.jspcafe.article.dao.ArticleDao;
import com.hyeonuk.jspcafe.article.dao.MysqlArticleDao;
import com.hyeonuk.jspcafe.article.domain.Article;
import com.hyeonuk.jspcafe.global.db.DBConnectionInfo;
import com.hyeonuk.jspcafe.global.db.DBManagerIml;
import com.hyeonuk.jspcafe.global.domain.Page;
import com.hyeonuk.jspcafe.member.dao.MemberDao;
import com.hyeonuk.jspcafe.member.dao.MysqlMemberDao;
import com.hyeonuk.jspcafe.member.domain.Member;
import com.hyeonuk.jspcafe.member.servlets.mock.*;
import com.hyeonuk.jspcafe.reply.dao.MysqlReplyDao;
import com.hyeonuk.jspcafe.reply.dao.ReplyDao;
import com.hyeonuk.jspcafe.reply.domain.Reply;
import com.hyeonuk.jspcafe.reply.dto.ReplyDto;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ReplyControlServlet 클래스")
class ReplyControlServletTest {
    private ReplyControlServlet replyControlServlet;
    private ReplyDao replyDao;
    private ArticleDao articleDao;
    private MemberDao memberDao;
    private ObjectMapper objectMapper;
    private MockRequest req;
    private MockResponse res;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException, ServletException {
        DBConnectionInfo connectionInfo = new DBConnectionInfo("application-testdb.yml");
        DBManagerIml manager = new DBManagerIml(connectionInfo);
        articleDao = new MysqlArticleDao(manager);
        replyDao = new MysqlReplyDao(manager);
        memberDao = new MysqlMemberDao(manager);
        objectMapper = new ObjectMapper();
        MockServletContext servletContext = new MockServletContext();
        BaseServletConfig servletConfig = new BaseServletConfig(servletContext);
        servletContext.setAttribute("articleDao",articleDao);
        servletContext.setAttribute("replyDao",replyDao);
        servletContext.setAttribute("objectMapper",objectMapper);

        replyControlServlet = new ReplyControlServlet();
        replyControlServlet.init(servletConfig);

        req = new MockRequest();
        res = new MockResponse();
    }

    @DisplayName("doGet 메서드는")
    @Nested
    class DoGet {
        @Test
        @DisplayName("articleId의 댓글들을 모두 불러올 수 있다.")
        public void canLoadAllReplies() throws Exception {
            //given
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Member replier1 = new Member(2l,"id2","pw2","nick2","email2");
            Member replier2 = new Member(3l,"id3","pw3","nick3","email3");
            Member replier3 = new Member(4l,"id4","pw4","nick4","email4");
            Article article = new Article(1l,member,"title!","contents!");
            memberDao.save(member);
            memberDao.save(replier1);
            memberDao.save(replier2);
            memberDao.save(replier3);
            articleDao.save(article);
            Reply reply1 = new Reply(article,replier1,"reply1");
            Reply reply2 = new Reply(article,replier2,"reply2");
            Reply reply3 = new Reply(article,replier3,"reply3");
            List<Reply> replies = List.of(reply1,reply2,reply3);
            replyDao.save(reply1);
            replyDao.save(reply2);
            replyDao.save(reply3);
            List<ReplyDto> expectedList = replies.stream()
                    .map(ReplyDto::from)
                    .collect(Collectors.toList());
            Page<ReplyDto> expected = new Page(5,1,replies.size(),expectedList);

            req.setPathInfo("/"+article.getId());

            //when
            replyControlServlet.doGet(req,res);

            //then
            String body = res.getBody();
            assertEquals(objectMapper.writeValueAsString(expected),body);
        }
    }

    @DisplayName("doPost 메서드는")
    @Nested
    class DoPost{
        @DisplayName("댓글을 저장할 수 있다.")
        @Test
        void replySaveTest() throws Exception {
            //given
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Article article = new Article(1l,member,"title","contents");
            memberDao.save(member);
            articleDao.save(article);

            MockSession session = new MockSession();
            session.setAttribute("member",member);
            req.setSession(session);

            String contents = "contents";
            String requestJson = "{\"contents\":\""+contents+"\",\"articleId\":"+article.getId()+"}";
            req.setInputStream(new ByteArrayInputStream(requestJson.getBytes()));

            //when
            replyControlServlet.doPost(req,res);

            //then
            String response = res.getBody();
            ReplyDto responseReply = objectMapper.readValue(response, ReplyDto.class);
            assertEquals(contents,responseReply.getContents());
            assertEquals(article.getId(),responseReply.getArticleId());
            assertTrue(replyDao.findById(responseReply.getReplyId()).isPresent());
        }
    }

    @Nested
    @DisplayName("doDelete메서드는")
    class DoDelete{
        @Test
        @DisplayName("자신의 댓글을 삭제하면 삭제된다.")
        void deleteMyReplyTest() throws Exception {
            //given
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Article article = new Article(1l,member,"title","contents");
            Reply reply = new Reply(1l,article,member,"reply1");
            memberDao.save(member);
            articleDao.save(article);
            replyDao.save(reply);

            MockSession session = new MockSession();
            session.setAttribute("member",member);
            req.setSession(session);

            req.setPathInfo("/"+reply.getId());

            //when
            replyControlServlet.doDelete(req,res);

            //then
            assertTrue(replyDao.findById(reply.getId()).isEmpty());
        }

        @Test
        @DisplayName("자신의 댓글이 아닌 것은 삭제할 수 없다.")
        void deleteOtherReplyTest() throws Exception{
            //given
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Article article = new Article(1l,member,"title","contents");
            Reply reply = new Reply(1l,article,member,"reply1");
            memberDao.save(member);
            articleDao.save(article);
            replyDao.save(reply);

            MockSession session = new MockSession();
            Member other = new Member(2l,"id2","pw2","nick2","email2");
            session.setAttribute("member",other);
            req.setSession(session);
            req.setPathInfo("/"+reply.getId());


            //when
            replyControlServlet.doDelete(req,res);

            //then
            int status = res.getStatus();
            assertEquals(SC_BAD_REQUEST,status);
            assertTrue(replyDao.findById(reply.getId()).isPresent());
        }

        @Test
        @DisplayName("없는 댓글을 삭제하면 exception을 던져 message를 전송한다.")
        void deleteNoneExistsReplyTest() throws Exception{
            //given
            Member member = new Member(1l,"id1","pw1","nick1","email1");
            Article article = new Article(1l,member,"title","contents");
            memberDao.save(member);
            articleDao.save(article);

            MockSession session = new MockSession();
            session.setAttribute("member",member);
            req.setSession(session);
            req.setPathInfo("/"+1);


            //when
            replyControlServlet.doDelete(req,res);

            //then
            int status = res.getStatus();
            assertEquals(HttpServletResponse.SC_BAD_REQUEST,status);
        }
    }

    private class MockRequest extends BaseHttpServletRequest {
        private Map<String, Object> attributes = new HashMap<>();
        private String forwardPath;
        private String pathInfo;
        private Map<String, String> parameters = new HashMap<>();
        private MockSession session;
        private InputStream is;
        public void setInputStream(InputStream is) {
            this.is = is;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read() throws IOException {
                    return is.read();
                }
            };
        }

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
        private String body = null;
        private ByteArrayOutputStream os = new ByteArrayOutputStream();
        private PrintWriter writer = new PrintWriter(os);
        private int status;

        @Override
        public void setStatus(int i) {
            this.status = i;
        }


        @Override
        public int getStatus() {
            return status;
        }

        @Override
        public void sendRedirect(String redirection) {
            this.redirection = redirection;
        }

        public String getRedirection() {
            return this.redirection;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return writer;
        }

        public String getBody(){
            byte[] byteArray = os.toByteArray();
            return new String(byteArray);
        }
    }
}