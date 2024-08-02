package woowa.camp.jspcafe.web.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.fixture.UserFixture;
import woowa.camp.jspcafe.web.exception.AuthenticationException;
import woowa.camp.jspcafe.web.mock.TestFilterChain;
import woowa.camp.jspcafe.web.mock.TestHttpServletRequest;
import woowa.camp.jspcafe.web.mock.TestHttpServletResponse;
import woowa.camp.jspcafe.web.mock.TestHttpSession;

class AuthenticationFilterTest {

    AuthenticationFilter filter = new AuthenticationFilter();

    @Nested
    @DisplayName("Describe_인증이 필요하지 않은")
    class UnAuthenticateAPITest {

        @ParameterizedTest
        @CsvSource(value =
                {"/users/registration;회원가입",
                        "/users/login;로그인",
                        "/users/login/fail;로그인실패",
                        "/;메인",
                        "/articles?page=;게시글목록조회",
                        "/static;정적리소스",
                        "/static/;정적리소스",
                        "/static/source;정적리소스",
                        "/static/source.img;정적리소스"}, delimiter = ';')
        @DisplayName("URI는 인증 필터를 거치지 않는다")
        void test(String requestURI) throws ServletException, IOException {
            // given
            TestHttpServletRequest request = new TestHttpServletRequest();
            TestHttpServletResponse response = new TestHttpServletResponse();
            request.setRequestURI(requestURI);
            FilterChain chain = (req, res) -> {
            };
            // when
            filter.doFilter(request, response, chain);
            // then
            assertThat(response.getRedirectLocation()).isNull();
        }

    }

    @Nested
    @DisplayName("Describe_인증이 필요한")
    class AuthenticateAPITest {

        @ParameterizedTest
        @CsvSource(value =
                {"/users;회원목록조회",
                        "/users/1;회원상세조회",
                        "/users/edit/1;회원정보수정",
                        "/users/logout;로그아웃",
                        "/articles/2;게시글상세조회",
                        "/articles/write;게시글작성"}, delimiter = ';')
        @DisplayName("URI는 쿠키가 없으면 인증을 실패한다")
        void test(String requestURI) throws ServletException, IOException {
            // given
            TestHttpServletRequest request = new TestHttpServletRequest();
            TestHttpServletResponse response = new TestHttpServletResponse();
            request.setRequestURI(requestURI);
            FilterChain chain = (req, res) -> {
            };
            // when then
            assertThatThrownBy(() -> filter.doFilter(request, response, chain))
                    .isInstanceOf(AuthenticationException.class);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "/users;회원목록조회",
                "/users/1;회원상세조회",
                "/users/edit/1;회원정보수정",
                "/users/logout;로그아웃",
                "/articles/2;게시글상세조회",
                "/articles/write;게시글작성"}, delimiter = ';')
        @DisplayName("URI는 로그인 쿠키가 없으면 인증을 실패한다")
        void test2(String requestURI) throws ServletException, IOException {
            // given
            TestHttpServletRequest request = new TestHttpServletRequest();
            TestHttpServletResponse response = new TestHttpServletResponse();
            request.setRequestURI(requestURI);
            request.setCookies(new Cookie[]{new Cookie("OTHER_COOKIE", "value")});
            FilterChain chain = (req, res) -> {
            };
            // when then
            assertThatThrownBy(() -> filter.doFilter(request, response, chain))
                    .isInstanceOf(AuthenticationException.class);
        }


        @ParameterizedTest
        @CsvSource(value = {
                "/users;회원목록조회",
                "/users/1;회원상세조회",
                "/users/edit/1;회원정보수정",
                "/users/logout;로그아웃",
                "/articles/2;게시글상세조회",
                "/articles/write;게시글작성"}, delimiter = ';')
        @DisplayName("URI는 로그인 쿠키이름에 해당하는 세션키가 없으면 인증을 실패한다")
        void test3(String requestURI) throws ServletException, IOException {
            // given
            TestHttpServletRequest request = new TestHttpServletRequest();
            TestHttpServletResponse response = new TestHttpServletResponse();
            request.setRequestURI(requestURI);
            request.setCookies(new Cookie[]{new Cookie("INVALID_COOKIE_NAME", "1")});
            FilterChain chain = (req, res) -> {
            };
            HttpSession session = request.getSession();
            session.setAttribute("WOOWA_SESSIONID", null);
            // when then
            assertThatThrownBy(() -> filter.doFilter(request, response, chain))
                    .isInstanceOf(AuthenticationException.class);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "/users;회원목록조회",
                "/users/1;회원상세조회",
                "/users/edit/1;회원정보수정",
                "/users/logout;로그아웃",
                "/articles/2;게시글상세조회",
                "/articles/write;게시글작성"}, delimiter = ';')
        @DisplayName("URI는 로그인 쿠키의 값인 사용자 ID와 세션값의 User가 일치하지 않으면 인증을 실패한다")
        void test4(String requestURI) throws ServletException, IOException {
            // given
            TestHttpServletRequest request = new TestHttpServletRequest();
            TestHttpServletResponse response = new TestHttpServletResponse();
            request.setRequestURI(requestURI);
            FilterChain chain = (req, res) -> {
            };
            HttpSession session = request.getSession();
            session.setAttribute("WOOWA_SESSIONID", createUserWithId(1L));
            // when
            request.setCookies(new Cookie[]{new Cookie("WOOWA_SESSIONID", "2")});
            // then
            assertThatThrownBy(() -> filter.doFilter(request, response, chain))
                    .isInstanceOf(AuthenticationException.class);
            // when
            request.setCookies(new Cookie[]{new Cookie("WOOWA_SESSIONID", "이상한값")});
            // then
            assertThatThrownBy(() -> filter.doFilter(request, response, chain))
                    .isInstanceOf(AuthenticationException.class);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "/users;회원목록조회",
                "/users/1;회원상세조회",
                "/users/edit/1;회원정보수정",
                "/users/logout;로그아웃",
                "/articles/2;게시글상세조회",
                "/articles/write;게시글작성"}, delimiter = ';')
        @DisplayName("URI는 현재 세션이 없는 상태면 인증을 실패한다")
        void test5(String requestURI) throws ServletException, IOException {
            // given
            TestHttpServletRequest request = new TestHttpServletRequest();
            TestHttpServletResponse response = new TestHttpServletResponse();
            request.setRequestURI(requestURI);
            request.setCookies(new Cookie[]{new Cookie("WOOWA_SESSIONID", "2")});
            FilterChain chain = (req, res) -> {
            };
            HttpSession session = null;
            //when then
            assertThatThrownBy(() -> filter.doFilter(request, response, chain))
                    .isInstanceOf(AuthenticationException.class);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "/users;회원목록조회",
                "/users/1;회원상세조회",
                "/users/edit/1;회원정보수정",
                "/users/logout;로그아웃",
                "/articles/2;게시글상세조회",
                "/articles/write;게시글작성"}, delimiter = ';')
        @DisplayName("URI는 현재 세션의 값이 없다면 인증을 실패한다")
        void test6(String requestURI) throws ServletException, IOException {
            // given
            TestHttpServletRequest request = new TestHttpServletRequest();
            TestHttpServletResponse response = new TestHttpServletResponse();
            request.setRequestURI(requestURI);
            request.setCookies(new Cookie[]{new Cookie("WOOWA_SESSIONID", "2")});
            FilterChain chain = (req, res) -> {
            };
            HttpSession session = request.getSession();
            session.setAttribute("WOOWA_SESSIONID", null);
            //when then
            assertThatThrownBy(() -> filter.doFilter(request, response, chain))
                    .isInstanceOf(AuthenticationException.class);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "/users;회원목록조회",
                "/users/1;회원상세조회",
                "/users/edit/1;회원정보수정",
                "/users/logout;로그아웃",
                "/articles/2;게시글상세조회",
                "/articles/write;게시글작성"}, delimiter = ';')
        @DisplayName("URI는 쿠키 이름의 세션 값이 User 가 아니면 인증을 실패한다")
        void test7(String requestURI) throws ServletException, IOException {
            // given
            TestHttpServletRequest request = new TestHttpServletRequest();
            TestHttpServletResponse response = new TestHttpServletResponse();
            request.setRequestURI(requestURI);
            FilterChain chain = (req, res) -> {
            };
            HttpSession session = request.getSession();
            // when then
            session.setAttribute("WOOWA_SESSIONID", new Object());
            request.setCookies(new Cookie[]{new Cookie("WOOWA_SESSIONID", "2")});
            assertThatThrownBy(() -> filter.doFilter(request, response, chain))
                    .isInstanceOf(AuthenticationException.class);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "/users;회원목록조회",
                "/users/1;회원상세조회",
                "/users/edit/1;회원정보수정",
                "/users/logout;로그아웃",
                "/articles/2;게시글상세조회",
                "/articles/write;게시글작성"}, delimiter = ';')
        @DisplayName("URI는 쿠키가 존재하고 세션도 올바른 경우 체인을 호출한다")
        void testChainDoFilterCalled(String requestURI) throws ServletException, IOException {
            // given
            TestHttpServletRequest request = new TestHttpServletRequest();
            TestHttpServletResponse response = new TestHttpServletResponse();
            request.setRequestURI(requestURI);
            request.setCookies(new Cookie[]{new Cookie("WOOWA_SESSIONID", "1")});
            HttpSession session = new TestHttpSession();
            session.setAttribute("WOOWA_SESSIONID", createUserWithId(1L)); // 올바른 세션
            request.setSession(session);
            TestFilterChain chain = new TestFilterChain();
            // when
            filter.doFilter(request, response, chain);
            // then
            assertThat(chain.isFilterCalled()).isTrue();
        }

        private User createUserWithId(long id) {
            User user1 = UserFixture.createUser1();
            user1.setId(id);
            return user1;
        }


    }
}