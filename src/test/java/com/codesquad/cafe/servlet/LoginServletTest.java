package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.TestUtil.getSessionIdFromSetCookieHeader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codesquad.cafe.E2ETestBase;
import com.codesquad.cafe.SavedHttpResponse;
import com.codesquad.cafe.db.dao.UserRepository;
import com.codesquad.cafe.db.domain.User;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginServletTest extends E2ETestBase {

    private static UserRepository userRepository;

    private final String path = "/login";

    private User user;

    @BeforeAll
    static void beforeAll() {
        userRepository = (UserRepository) context.getServletContext().getAttribute("userRepository");
    }

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.of("javajigi", "1234", "박재성", "test@gmail.com"));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인폼을 반환한다.")
    void testDoGetLoginForm() throws IOException {
        //when
        SavedHttpResponse response = get(path);
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertTrue(html.contains("<form name=\"login\" method=\"post\" action=\"/login\">"));
        assertTrue(html.contains("username"));
        assertTrue(html.contains("password"));
    }

    @Test
    @DisplayName("로그인에 성공하면 세션에 userPrincipal이 저장되고 로그아웃 개인정보 수정 버튼이 표시된다.")
    void tesLogin() throws IOException {
        //when
        HttpResponse response = post(path, createFormData(user.getUsername(), user.getPassword()));

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertTrue(response.getFirstHeader("Set-Cookie").getValue().contains("JSESSIONID"));
        assertEquals("/", response.getFirstHeader("Location").getValue());
    }

    @Test
    @DisplayName("세션에 redirectAfterLogin이 있다면 요청 페이지로 리다이렉트한다.")
    void tesRedirectAfterLogin() throws IOException {
        //given
        String originalRequestUrl = "/posts";
        HttpResponse post = post(originalRequestUrl, "authorId=1L&title=제목&content=내용");
        String sessionId = getSessionIdFromSetCookieHeader(post.getFirstHeader("Set-Cookie").getValue());
        //when
        HttpResponse response = post(path, createFormData(user.getUsername(), user.getPassword()), sessionId);

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertEquals(originalRequestUrl, response.getFirstHeader("Location").getValue());
    }

    @Test
    @DisplayName("비밀번호가 틀리면 로그인 폼으로 이동하고 로그인 실패 경고 메시지를 표시한다.")
    void testLoginFailWrongPassword() throws IOException, URISyntaxException {
        //when
        HttpResponse response = post(path, createFormData(user.getUsername(), "wrong password"));
        String html = EntityUtils.toString(response.getEntity());

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getEntity().getContentType().getValue());
        assertTrue(html.contains("<form name=\"login\" method=\"post\" action=\"/login\">"));
        assertTrue(html.contains("아이디 또는 비밀번호가 틀립니다. 다시 로그인 해주세요."));
        assertTrue(html.contains("username"));
        assertTrue(html.contains("password"));
    }

    @Test
    @DisplayName("아이디가 null이면 로그인 폼으로 이동하고 로그인 실패 경고 메시지를 표시한다.")
    void testLoginFailNullUsername() throws IOException {
        //when
        HttpResponse response = post(path, createFormData(null, user.getPassword()));
        String html = EntityUtils.toString(response.getEntity());

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getEntity().getContentType().getValue());
        assertTrue(html.contains("<form name=\"login\" method=\"post\" action=\"/login\">"));
        assertTrue(html.contains("아이디 또는 비밀번호가 틀립니다. 다시 로그인 해주세요."));
        assertTrue(html.contains("username"));
        assertTrue(html.contains("password"));
    }

    @Test
    @DisplayName("비밀번호가 null이면 로그인 폼으로 이동하고 로그인 실패 경고 메시지를 표시한다.")
    void testLoginFailNullPassword() throws IOException {
        //when
        HttpResponse response = post(path, createFormData(user.getUsername(), null));
        String html = EntityUtils.toString(response.getEntity());

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getEntity().getContentType().getValue());
        assertTrue(html.contains("<form name=\"login\" method=\"post\" action=\"/login\">"));
        assertTrue(html.contains("아이디 또는 비밀번호가 틀립니다. 다시 로그인 해주세요."));
        assertTrue(html.contains("username"));
        assertTrue(html.contains("password"));
    }

    @Test
    @DisplayName("아이디가 누락되면 로그인 폼으로 이동하고 로그인 실패 경고 메시지를 표시한다.")
    void testLoginFailAbsentPassword() throws IOException {
        //when
        HttpResponse response = post(path, "password=1234");
        String html = EntityUtils.toString(response.getEntity());

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getEntity().getContentType().getValue());
        assertTrue(html.contains("<form name=\"login\" method=\"post\" action=\"/login\">"));
        assertTrue(html.contains("아이디 또는 비밀번호가 틀립니다. 다시 로그인 해주세요."));
        assertTrue(html.contains("username"));
        assertTrue(html.contains("password"));
    }

    @Test
    @DisplayName("비밀번호가 누락되면 로그인 폼으로 이동하고 로그인 실패 경고 메시지를 표시한다.")
    void testLoginFailAbsentUsername() throws IOException {
        //when
        HttpResponse response = post(path, "usernmae" + user.getUsername());
        String html = EntityUtils.toString(response.getEntity());

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getEntity().getContentType().getValue());
        assertTrue(html.contains("<form name=\"login\" method=\"post\" action=\"/login\">"));
        assertTrue(html.contains("아이디 또는 비밀번호가 틀립니다. 다시 로그인 해주세요."));
        assertTrue(html.contains("username"));
        assertTrue(html.contains("password"));
    }

    @Test
    @DisplayName("탈퇴한 유저로 로그인시 실패한다.")
    void testLoginFailDeletedUser() throws IOException {
        //given
        user.delete();
        userRepository.save(user);

        //when
        HttpResponse response = post(path, createFormData(user.getUsername(), user.getPassword()));
        String html = EntityUtils.toString(response.getEntity());

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getEntity().getContentType().getValue());
        assertTrue(html.contains("<form name=\"login\" method=\"post\" action=\"/login\">"));
        assertTrue(html.contains("탈퇴한 유저입니다."));
        assertTrue(html.contains("username"));
        assertTrue(html.contains("password"));
    }

    private String createFormData(String username, String password) {
        StringBuilder sb = new StringBuilder();
        sb.append("username");
        sb.append("=");
        if (username != null) {
            sb.append(username);
        }
        sb.append("&password=");
        if (password != null) {
            sb.append(password);
        }
        return sb.toString();
    }

}
