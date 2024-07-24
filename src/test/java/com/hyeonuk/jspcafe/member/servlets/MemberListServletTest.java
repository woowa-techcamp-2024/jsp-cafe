package com.hyeonuk.jspcafe.member.servlets;

import com.hyeonuk.jspcafe.global.utils.PasswordEncoder;
import com.hyeonuk.jspcafe.member.dao.InMemoryMemberDao;
import com.hyeonuk.jspcafe.member.dao.MemberDao;
import com.hyeonuk.jspcafe.member.domain.Member;
import com.hyeonuk.jspcafe.member.servlets.mock.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("MemberListServlet 클래스")
class MemberListServletTest {
    private MemberDao memberDao;
    private MemberListServlet servlet;
    @BeforeEach
    void servletSetUp() throws ServletException {
        memberDao = new InMemoryMemberDao();
        ServletContext context = new MockServletContext();
        context.setAttribute("memberDao",memberDao);
        ServletConfig servletConfig = new BaseServletConfig(context);
        servlet = new MemberListServlet();
        servlet.init(servletConfig);
    }

    @Nested
    @DisplayName("doGet메서드는")
    class DoGet{
        @Test
        @DisplayName("정상적인 요청이 들어오면 req에 member 리스트가 저장되고 /user/list.jsp로 포워딩된다.")
        void successWithMembers() throws Exception{
            //given
            Member member1 = new Member(1l,"id1","pw1","nick1","email1");
            Member member2 = new Member(2l,"id2","pw2","nick2","email2");
            Member member3 = new Member(3l,"id3","pw3","nick3","email3");
            Member member4 = new Member(4l,"id4","pw4","nick4","email4");
            memberDao.save(member1);
            memberDao.save(member2);
            memberDao.save(member3);
            memberDao.save(member4);
            List<Member> saveMembers = List.of(member1,member2,member3,member4);
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();

            //when
            servlet.doGet(req,res);

            //then
            Object attribute = req.getAttribute("members");
            assertNotNull(attribute);
            List<Member> members = (List<Member>) attribute;
            assertEquals(saveMembers,members);
            assertEquals("/templates/user/list.jsp",req.getForwardPath());
        }
    }

    private class MockRequest extends BaseHttpServletRequest {
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