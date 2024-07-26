package com.hyeonuk.jspcafe.member.servlets;

import com.hyeonuk.jspcafe.global.exception.HttpBadRequestException;
import com.hyeonuk.jspcafe.global.exception.HttpNotFoundException;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MemberLoginServletTest {
    private MemberDao memberDao;
    private MemberLoginServlet servlet;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void servletSetUp() throws ServletException {
        memberDao = new InMemoryMemberDao();
        passwordEncoder = new MockPasswordEncoder();
        ServletContext context = new MockServletContext();
        context.setAttribute("memberDao", memberDao);
        context.setAttribute("passwordEncoder", passwordEncoder);
        ServletConfig servletConfig = new BaseServletConfig(context);
        servlet = new MemberLoginServlet();
        servlet.init(servletConfig);
    }

    @Nested
    @DisplayName("doGet메서드는")
    class DoGet {
        @Test
        @DisplayName("정상적인 요청이 들어오면 req에 member 리스트가 저장되고 /user/login.jsp로 포워딩된다.")
        void success() throws Exception {
            //given
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();

            //when
            servlet.doGet(req, res);

            //then
            assertEquals("/templates/user/login.jsp", req.getForwardPath());
        }
    }

    @Nested
    @DisplayName("doPost메서드는")
    class DoPost {
        @Test
        @DisplayName("정상적으로 로그인하면 session에 member객체가 저장된 후 / 로 리다이렉트된다.")
        void successWithMember() throws Exception {
            //given
            Member member = new Member(1l, "id1", passwordEncoder.encode("pw1"), "nick1", "email1");
            memberDao.save(member);
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();
            MockSession session = new MockSession();
            req.setSession(session);
            req.setParameter("userId", member.getMemberId());
            req.setParameter("password", "pw1");

            //when
            servlet.doPost(req, res);

            //then
            HttpSession savedSession = req.getSession();
            assertNotNull(savedSession);
            assertNotNull(savedSession.getAttribute("member"));
            Member sessionMember = (Member)savedSession.getAttribute("member");
            compareMember(member, sessionMember);
            assertEquals("/",res.getRedirect());
        }

        @Test
        @DisplayName("존재하지 않는 유저면 404 오류를 던진다.")
        void notExistsMember() throws Exception {
            //given
            Member member = new Member(1l, "id1", passwordEncoder.encode("pw1"), "nick1", "email1");
            memberDao.save(member);
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();
            MockSession session = new MockSession();
            req.setSession(session);
            req.setParameter("userId", "notExistMember");
            req.setParameter("password", "pw1");

            //when & then
            assertThrows(HttpNotFoundException.class,()->{
                servlet.doPost(req, res);
            });
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 400 오류를 던진다.")
        void passwordNotMatch() throws Exception {
            //given
            Member member = new Member(1l, "id1", passwordEncoder.encode("pw1"), "nick1", "email1");
            memberDao.save(member);
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();
            MockSession session = new MockSession();
            req.setSession(session);
            req.setParameter("userId", member.getMemberId());
            req.setParameter("password", "wrongPassword");

            //when & then
            assertThrows(HttpBadRequestException.class,()->{
                servlet.doPost(req, res);
            });
        }
    }

    private static void compareMember(Member expected, Member actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getMemberId(), actual.getMemberId());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    private class MockRequest extends BaseHttpServletRequest {
        private String forwardPath;
        private Map<String, String> parameters = new HashMap<>();
        private HttpSession session;

        public void setSession(HttpSession session) {
            this.session = session;
        }

        @Override
        public HttpSession getSession() {
            return session;
        }

        @Override
        public HttpSession getSession(boolean create) {
            if (create && session == null) {
                return new MockSession();
            }
            return session;
        }

        @Override
        public String getParameter(String s) {
            return parameters.get(s);
        }

        public void setParameter(String key, String value) {
            parameters.put(key, value);
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
        public void sendRedirect(String location) {
            redirection = location;
        }
        public String getRedirect() {
            return redirection;
        }
    }
}