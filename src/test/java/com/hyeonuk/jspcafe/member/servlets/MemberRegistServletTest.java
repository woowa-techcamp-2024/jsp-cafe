package com.hyeonuk.jspcafe.member.servlets;

import com.hyeonuk.jspcafe.global.exception.InvalidMemberRegistRequest;
import com.hyeonuk.jspcafe.global.utils.PasswordEncoder;
import com.hyeonuk.jspcafe.member.dao.InMemoryMemberDao;
import com.hyeonuk.jspcafe.member.dao.MemberDao;
import com.hyeonuk.jspcafe.member.domain.Member;
import com.hyeonuk.jspcafe.member.servlets.mock.*;
import jakarta.servlet.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MemberRegistServlet 클래스")
class MemberRegistServletTest {
    private MemberDao memberDao;
    private MemberRegistServlet servlet;
    private PasswordEncoder passwordEncoder;
    @BeforeEach
    void servletSetUp() throws ServletException {
        memberDao = new InMemoryMemberDao();
        passwordEncoder = new MockPasswordEncoder();
        ServletContext context = new MockServletContext();
        context.setAttribute("memberDao",memberDao);
        context.setAttribute("passwordEncoder",passwordEncoder);
        ServletConfig servletConfig = new BaseServletConfig(context);
        servlet = new MemberRegistServlet();
        servlet.init(servletConfig);
    }

    @Nested
    @DisplayName("doGet메서드를 실행하면")
    class DoGetMethod{
        @DisplayName("user/form.jsp로 forwarding")
        @Test
        void requestTest() throws Exception {
            //given
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();

            //when
            servlet.doGet(req,res);

            //then
            String forwardPath = req.getForwardPath();
            assertEquals("/templates/user/form.jsp",forwardPath);
        }
    }

    @Nested
    @DisplayName("doPost메서드를 실행하면")
    class DoPostMethod{
        @DisplayName("정상적인 요청은 정상적으로 처리된다.")
        @Test
        void postTest() throws Exception{
            //given
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();
            req.setParameter("userId","rlagusdnr120");
            req.setParameter("password","password1234");
            req.setParameter("nickname","khu147");
            req.setParameter("email","rlagusdnr120@gmail.com");

            //when
            servlet.doPost(req,res);

            //then
            Optional<Member> byMemberId = memberDao.findByMemberId("rlagusdnr120");
            String redirectPath = res.getRedirectUrl();
            assertTrue(byMemberId.isPresent());
            Member find = byMemberId.get();
            assertEquals("rlagusdnr120@gmail.com",find.getEmail());
            assertEquals(passwordEncoder.encode("password1234"),find.getPassword());
            assertEquals("rlagusdnr120",find.getMemberId());
            assertEquals("khu147",find.getNickname());
            assertEquals("/members",redirectPath);
        }

        @Test
        @DisplayName("아이디가 null이 들어오면 오류를 반환한다.")
        void idNullTest() throws Exception{
            //given
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();
            req.setParameter("userId",null);
            req.setParameter("password","password1234");
            req.setParameter("nickname","khu147");
            req.setParameter("email","rlagusdnr120@gmail.com");

            //when & then
            assertThrows(InvalidMemberRegistRequest.class,()->{
                servlet.doPost(req,res);
            });
        }

        @Test
        @DisplayName("아이디가 blank면 들어오면 오류를 반환한다.")
        void idBlankTest() throws Exception{
            //given
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();
            req.setParameter("userId","");
            req.setParameter("password","password1234");
            req.setParameter("nickname","khu147");
            req.setParameter("email","rlagusdnr120@gmail.com");

            //when & then
            assertThrows(InvalidMemberRegistRequest.class,()->{
                servlet.doPost(req,res);
            });
        }

        @Test
        @DisplayName("비밀번호가 null이 들어오면 오류를 반환한다.")
        void passwordNullTest() throws Exception{
            //given
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();
            req.setParameter("userId","rlagusdnr120");
            req.setParameter("password",null);
            req.setParameter("nickname","khu147");
            req.setParameter("email","rlagusdnr120@gmail.com");

            //when & then
            assertThrows(InvalidMemberRegistRequest.class,()->{
                servlet.doPost(req,res);
            });
        }

        @Test
        @DisplayName("비밀번호가 blank로 들어오면 오류를 반환한다.")
        void passwordBlankTest() throws Exception{
            //given
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();
            req.setParameter("userId","rlagusdnr120");
            req.setParameter("password","");
            req.setParameter("nickname","khu147");
            req.setParameter("email","rlagusdnr120@gmail.com");

            //when & then
            assertThrows(InvalidMemberRegistRequest.class,()->{
                servlet.doPost(req,res);
            });
        }
        @Test
        @DisplayName("닉네임에 null이 들어오면 오류를 반환한다.")
        void nicknameNullTest() throws Exception{
            //given
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();
            req.setParameter("userId","rlagusdnr120");
            req.setParameter("password","password1234");
            req.setParameter("nickname",null);
            req.setParameter("email","rlagusdnr120@gmail.com");

            //when & then
            assertThrows(InvalidMemberRegistRequest.class,()->{
                servlet.doPost(req,res);
            });
        }

        @Test
        @DisplayName("닉네임에 blank값이 들어오면 오류를 반환한다.")
        void nicknameBlankTest() throws Exception{
            //given
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();
            req.setParameter("userId","rlagusdnr120");
            req.setParameter("password","password1234");
            req.setParameter("nickname","");
            req.setParameter("email","rlagusdnr120@gmail.com");

            //when & then
            assertThrows(InvalidMemberRegistRequest.class,()->{
                servlet.doPost(req,res);
            });
        }

        @Test
        @DisplayName("이메일에 null이 들어오면 오류를 반환한다.")
        void emailNullTest() throws Exception{
            //given
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();
            req.setParameter("userId","rlagusdnr120");
            req.setParameter("password","password1234");
            req.setParameter("nickname","khu147");
            req.setParameter("email",null);

            //when & then
            assertThrows(InvalidMemberRegistRequest.class,()->{
                servlet.doPost(req,res);
            });
        }

        @Test
        @DisplayName("이메일에 blank값이 들어오면 오류를 반환한다.")
        void emailBlankTest() throws Exception{
            //given
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();
            req.setParameter("userId","rlagusdnr120");
            req.setParameter("password","password1234");
            req.setParameter("nickname","khu147");
            req.setParameter("email","");

            //when & then
            assertThrows(InvalidMemberRegistRequest.class,()->{
                servlet.doPost(req,res);
            });
        }

        @Test
        @DisplayName("이미 등록된 아이디로 회원가입하면 오류를 던진다.")
        void alreadyExistsUser() throws Exception{
            //given
            memberDao.save(new Member("rlagusdnr120","password12","already","already@gmail.com"));
            MockRequest req = new MockRequest();
            MockResponse res = new MockResponse();
            req.setParameter("userId","rlagusdnr120");
            req.setParameter("password","password1234");
            req.setParameter("nickname","khu147");
            req.setParameter("email","rlagusdnr120@gmail.com");

            //when & then
            assertThrows(InvalidMemberRegistRequest.class,()->{
                servlet.doPost(req,res);
            });
        }
    }

    private class MockRequest extends BaseHttpServletRequest {
        private Map<String,String> parameters = new HashMap<>();
        private String forwardPath;
        public void setParameter(String s, String o) {
            parameters.put(s, o);
        }

        @Override
        public String getParameter(String s) {
            return parameters.get(s);
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
        private String redirectUrl;

        @Override
        public void sendRedirect(String s) throws IOException {
            this.redirectUrl = s;
        }

        public String getRedirectUrl() {
            return this.redirectUrl;
        }
    }


}