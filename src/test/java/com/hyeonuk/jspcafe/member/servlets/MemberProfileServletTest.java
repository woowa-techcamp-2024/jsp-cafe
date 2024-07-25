package com.hyeonuk.jspcafe.member.servlets;

import com.hyeonuk.jspcafe.global.exception.HttpBadRequestException;
import com.hyeonuk.jspcafe.global.exception.HttpNotFoundException;
import com.hyeonuk.jspcafe.member.dao.InMemoryMemberDao;
import com.hyeonuk.jspcafe.member.dao.MemberDao;
import com.hyeonuk.jspcafe.member.domain.Member;
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

@DisplayName("MemberProfileServlet 클래스")
public class MemberProfileServletTest {
    private MemberDao memberDao;
    private MemberProfileServlet servlet;
    @BeforeEach
    void servletSetUp() throws ServletException {
        memberDao = new InMemoryMemberDao();
        ServletContext context = new MockServletContext();
        context.setAttribute("memberDao",memberDao);
        ServletConfig servletConfig = new BaseServletConfig(context);
        servlet = new MemberProfileServlet();
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

    private class MockRequest extends BaseHttpServletRequest {
        private Map<String,Object> attributes = new HashMap<>();
        private String forwardPath;
        private String pathInfo;
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

    }
}
