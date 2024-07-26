package com.hyeonuk.jspcafe.member.servlets;

import com.hyeonuk.jspcafe.member.dao.InMemoryMemberDao;
import com.hyeonuk.jspcafe.member.servlets.mock.*;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("MemberLogoutServlet 클래스")
class MemberLogoutServletTest {
    private MemberLogoutServlet servlet;
    @BeforeEach
    void servletSetUp() throws ServletException {
        ServletContext context = new MockServletContext();
        ServletConfig servletConfig = new BaseServletConfig(context);
        servlet = new MemberLogoutServlet();
        servlet.init(servletConfig);
    }

    @Nested
    @DisplayName("doPost 메서드는")
    class DoPost {
        @DisplayName("세션에 객체가 저장된 채로 로그아웃하면 invalidate 후 / 로 redirection.")
        @Test
        void success() throws ServletException, IOException {
            //given
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();
            MockSession session = new MockSession();
            req.setSession(session);

            //when
            servlet.doPost(req,res);

            //then
            assertTrue(session.isInvalid());
            assertEquals("/",res.getRedirection());
        }

        @DisplayName("세션이 없어도 / 로 redirection")
        @Test
        void nullSession() throws ServletException, IOException {
            //given
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();

            //when
            servlet.doPost(req,res);

            //then
            assertEquals("/",res.getRedirection());
        }
    }


    private class MockRequest extends BaseHttpServletRequest {
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
    }

    private class MockResponse extends BaseHttpServletResponse {
        private String redirection;
        @Override
        public void sendRedirect(String redirection) {
            this.redirection = redirection;
        }
        public String getRedirection() {
            return redirection;
        }
    }
}