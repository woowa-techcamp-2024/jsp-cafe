package com.hyeonuk.jspcafe.global.servlet.filter;

import com.hyeonuk.jspcafe.global.servlet.filter.mock.MockFilterChain;
import com.hyeonuk.jspcafe.member.domain.Member;
import com.hyeonuk.jspcafe.member.servlets.mock.BaseHttpServletRequest;
import com.hyeonuk.jspcafe.member.servlets.mock.BaseHttpServletResponse;
import com.hyeonuk.jspcafe.member.servlets.mock.MockSession;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Session의 인증과 관련된 필터 클래스")
class SessionFilterTest {
    private MockFilterChain filterChain;
    private MockRequest req;
    private MockResponse res;
    private Filter sessionFilter;

    @BeforeEach
    void setUp() {
        filterChain = new MockFilterChain();
        req = new MockRequest();
        res = new MockResponse();
        sessionFilter = new SessionFilter();
    }

    @Nested
    @DisplayName("session에 member 값이 있다면")
    class ExistsSessionValue {
        @DisplayName("다음 체인으로 넘어간다.")
        @Test
        void redirectToLogin() throws ServletException, IOException {
            //given
            HttpSession session = new MockSession();
            session.setAttribute("member", new Member(1l, "id1", "pw1", "nick1", "email1"));
            req.setSession(session);

            //when
            sessionFilter.doFilter(req, res, filterChain);

            //then
            assertEquals(1, filterChain.getCallCount());//넘어간다.
        }
    }

    @Nested
    @DisplayName("session이 null이라면")
    class NullSession {
        @DisplayName("다음 체인으로 가지 않고 /login으로 redirect된다.")
        @Test
        void redirectToLogin() throws ServletException, IOException {
            //given

            //when
            sessionFilter.doFilter(req, res, filterChain);

            //then
            assertEquals(0, filterChain.getCallCount());//넘어가지 못하고 redirection된다.
            assertEquals("/login", res.getRedirection());
        }
    }

    @Nested
    @DisplayName("session에 member 값이 없다면")
    class NullSessionValue {
        @DisplayName("다음 체인으로 가지 않고 /login으로 redirect된다.")
        @Test
        void redirectToLogin() throws ServletException, IOException {
            //given
            HttpSession session = new MockSession();
            req.setSession(session);

            //when
            sessionFilter.doFilter(req, res, filterChain);

            //then
            assertEquals(0, filterChain.getCallCount());//넘어가지 못하고 redirection된다.
            assertEquals("/login", res.getRedirection());
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
        public void sendRedirect(String s) throws IOException {
            this.redirection = s;
        }

        public String getRedirection() {
            return this.redirection;
        }
    }
}