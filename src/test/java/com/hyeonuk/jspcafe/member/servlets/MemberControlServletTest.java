package com.hyeonuk.jspcafe.member.servlets;

import com.hyeonuk.jspcafe.global.exception.HttpBadRequestException;
import com.hyeonuk.jspcafe.global.exception.HttpNotFoundException;
import com.hyeonuk.jspcafe.global.exception.InvalidMemberRegistRequest;
import com.hyeonuk.jspcafe.global.utils.PasswordEncoder;
import com.hyeonuk.jspcafe.member.dao.InMemoryMemberDao;
import com.hyeonuk.jspcafe.member.dao.MemberDao;
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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MemberProfileServlet 클래스")
public class MemberControlServletTest {
    private MemberDao memberDao;
    private PasswordEncoder passwordEncoder;
    private MemberControlServlet servlet;
    @BeforeEach
    void servletSetUp() throws ServletException {
        memberDao = new InMemoryMemberDao();
        passwordEncoder = new MockPasswordEncoder();
        ServletContext context = new MockServletContext();
        context.setAttribute("memberDao",memberDao);
        context.setAttribute("passwordEncoder",passwordEncoder);
        ServletConfig servletConfig = new BaseServletConfig(context);
        servlet = new MemberControlServlet();
        servlet.init(servletConfig);
    }

    @Nested
    @DisplayName("doGet 메서드는")
    class DoGet{
        @DisplayName("성공적으로 처리하면 원하는 사람이 나온다.")
        @Test
        void doGetTest() throws ServletException, IOException {
            //given
            Member member1 = new Member(1l,"id1","pw1","nick1","email1@gmail.com");
            Member member2 = new Member(2l,"id2","pw2","nick2","email2@gmail.com");
            Member member3 = new Member(3l,"id3","pw3","nick3","email3@gmail.com");
            Member member4 = new Member(4l,"id4","pw4","nick4","email4@gmail.com");
            Member member5 = new Member(5l,"id5","pw5","nick5","email5@gmail.com");
            List<Member> memberList = List.of(member1,member2,member3,member4,member5);
            memberList.forEach(memberDao::save);

            MockRequest req = new MockRequest();
            req.setPathInfo("/"+member1.getMemberId());
            MockResponse resp = new MockResponse();

            //when
            servlet.doGet(req,resp);

            //then
            Object attribute = req.getAttribute("member");
            assertNotNull(attribute);
            Member find = (Member) attribute;
            assertEquals(member1,find);
            assertEquals("/templates/user/profile.jsp",req.getForwardPath());
        }

        @DisplayName("프로필 수정 폼을 성공적으로 조회한다.")
        @Test
        void doGetProfileUpdateFormTest() throws ServletException, IOException {
            Member member1 = new Member(1L, "id1", "pw1", "nick1", "email1@gmail.com");
            memberDao.save(member1);

            MockRequest req = new MockRequest();
            req.setPathInfo("/" + member1.getMemberId() + "/form");
            MockResponse resp = new MockResponse();

            servlet.doGet(req, resp);

            Object attribute = req.getAttribute("member");
            assertNotNull(attribute);
            Member find = (Member) attribute;
            assertEquals(member1, find);
            assertEquals("/templates/user/profile_update.jsp", req.getForwardPath());
        }

        @DisplayName("path가 유효하지 않을때, 즉 /member/   로 들어올 경우 400오류를 던짐")
        @Test
        void invalidPathExceptionWithBlank() throws ServletException, IOException {
            //given
            Member member1 = new Member(1l,"id1","pw1","nick1","email1@gmail.com");
            Member member2 = new Member(2l,"id2","pw2","nick2","email2@gmail.com");
            Member member3 = new Member(3l,"id3","pw3","nick3","email3@gmail.com");
            Member member4 = new Member(4l,"id4","pw4","nick4","email4@gmail.com");
            Member member5 = new Member(5l,"id5","pw5","nick5","email5@gmail.com");
            List<Member> memberList = List.of(member1,member2,member3,member4,member5);
            memberList.forEach(memberDao::save);

            MockRequest req = new MockRequest();
            req.setPathInfo("/  ");
            MockResponse resp = new MockResponse();

            //when & then
            assertThrows(HttpBadRequestException.class,()->{
                servlet.doGet(req,resp);
            });
        }

        @DisplayName("path가 유효하지 않을때, 즉 pathInfo가 null로 들어올 경우 400오류를 던짐")
        @Test
        void invalidPathExceptionWithNull() throws ServletException, IOException {
            //given
            Member member1 = new Member(1l,"id1","pw1","nick1","email1@gmail.com");
            Member member2 = new Member(2l,"id2","pw2","nick2","email2@gmail.com");
            Member member3 = new Member(3l,"id3","pw3","nick3","email3@gmail.com");
            Member member4 = new Member(4l,"id4","pw4","nick4","email4@gmail.com");
            Member member5 = new Member(5l,"id5","pw5","nick5","email5@gmail.com");
            List<Member> memberList = List.of(member1,member2,member3,member4,member5);
            memberList.forEach(memberDao::save);

            MockRequest req = new MockRequest();
            req.setPathInfo(null);
            MockResponse resp = new MockResponse();

            //when & then
            assertThrows(HttpBadRequestException.class,()->{
                servlet.doGet(req,resp);
            });
        }

        @DisplayName("없는 유저를 찾으면 404 오류를 던짐")
        @Test
        void notExistsUser() throws ServletException, IOException {
            //given
            Member member1 = new Member(1l,"id1","pw1","nick1","email1@gmail.com");
            Member member2 = new Member(2l,"id2","pw2","nick2","email2@gmail.com");
            Member member3 = new Member(3l,"id3","pw3","nick3","email3@gmail.com");
            Member member4 = new Member(4l,"id4","pw4","nick4","email4@gmail.com");
            Member member5 = new Member(5l,"id5","pw5","nick5","email5@gmail.com");
            List<Member> memberList = List.of(member1,member2,member3,member4,member5);
            memberList.forEach(memberDao::save);

            MockRequest req = new MockRequest();
            req.setPathInfo("/notExists");
            MockResponse resp = new MockResponse();

            //when & then
            assertThrows(HttpNotFoundException.class,()->{
                servlet.doGet(req,resp);
            });
        }
    }

    @Nested
    @DisplayName("doPost 메서드는")
    class DoPost {
        @DisplayName("성공적으로 회원 정보를 수정한다")
        @Test
        void doPostSuccessfulUpdate() throws ServletException, IOException {
            Member member = new Member(1L, "id1", passwordEncoder.encode("pw1"), "nick1", "email1@gmail.com");
            memberDao.save(member);

            MockRequest req = new MockRequest();
            req.setPathInfo("/" + member.getMemberId());
            req.setParameter("passwordCheck", "pw1");
            req.setParameter("password", "newpw1");
            req.setParameter("email", "newemail1@gmail.com");
            req.setParameter("nickname", "newnick1");
            MockSession session = new MockSession();
            session.setAttribute("member",member);
            req.setSession(session);

            MockResponse resp = new MockResponse();

            servlet.doPost(req, resp);

            Member updatedMember = memberDao.findByMemberId("id1").orElseThrow();
            assertTrue(passwordEncoder.match("newpw1", updatedMember.getPassword()));
            assertEquals("newemail1@gmail.com", updatedMember.getEmail());
            assertEquals("newnick1", updatedMember.getNickname());
            assertEquals("/members/id1", resp.getRedirection());
        }

        @DisplayName("path가 유효하지 않을때, 즉 pathInfo가 null로 들어올 경우 400오류를 던짐")
        @Test
        void invalidPathExceptionWithNull() throws ServletException, IOException {
            //given
            MockRequest req = new MockRequest();
            req.setPathInfo(null);
            MockResponse resp = new MockResponse();

            //when & then
            assertThrows(HttpBadRequestException.class,()->{
                servlet.doPost(req,resp);
            });
        }

        @DisplayName("세션이 없으면 로그인 페이지로 리다이렉트한다")
        @Test
        void doPostNoSession() throws ServletException, IOException {
            MockRequest req = new MockRequest();
            req.setPathInfo("/id1");
            MockResponse resp = new MockResponse();

            servlet.doPost(req, resp);

            assertEquals("/login", resp.getRedirection());
        }

        @DisplayName("비밀번호가 일치하지 않으면 400 오류를 던짐")
        @Test
        void doPostIncorrectPassword() throws ServletException, IOException {
            Member member = new Member(1L, "id1",  passwordEncoder.encode("pw1"),"nick1", "email1@gmail.com");
            memberDao.save(member);

            MockRequest req = new MockRequest();
            req.setPathInfo("/" + member.getMemberId());
            req.setParameter("passwordCheck", "wrongpw");

            HttpSession session = new MockSession();
            session.setAttribute("member",member);
            req.setSession(session);

            MockResponse resp = new MockResponse();

            assertThrows(HttpBadRequestException.class, () -> servlet.doPost(req, resp));
        }

        @DisplayName("세션의 멤버 ID와 요청 경로의 멤버 ID가 일치하지 않으면 로그인 페이지로 리다이렉트한다")
        @Test
        void doPostMismatchedMemberId() throws ServletException, IOException {
            Member member = new Member(1L, "id1", passwordEncoder.encode("pw1"), "nick1", "email1@gmail.com");
            memberDao.save(member);

            MockRequest req = new MockRequest();
            req.setPathInfo("/id2");
            req.setParameter("passwordCheck", "pw1");

            HttpSession session = new MockSession();
            session.setAttribute("member",member);
            req.setSession(session);

            MockResponse resp = new MockResponse();

            servlet.doPost(req, resp);

            assertEquals("/login", resp.getRedirection());
        }

        @DisplayName("유효하지 않은 회원 정보로 수정을 시도하면 예외를 던짐")
        @Test
        void doPostInvalidMemberInfo() throws ServletException, IOException {
            Member member = new Member(1L, "id1", passwordEncoder.encode("pw1"), "nick1", "email1@gmail.com");
            memberDao.save(member);

            MockRequest req = new MockRequest();
            req.setPathInfo("/" + member.getMemberId());
            req.setParameter("passwordCheck", "pw1");
            req.setParameter("password", "");
            req.setParameter("email", "newemail1@gmail.com");
            req.setParameter("nickname", "newnick1");

            HttpSession session = new MockSession();
            session.setAttribute("member",member);
            req.setSession(session);

            MockResponse resp = new MockResponse();


            assertThrows(InvalidMemberRegistRequest.class, () -> servlet.doPost(req, resp));
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
        public void setPathInfo(String pathInfo){
            this.pathInfo = pathInfo;
        }
        @Override
        public String getPathInfo(){
            return this.pathInfo;
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
