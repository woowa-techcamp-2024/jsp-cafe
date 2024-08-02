package com.codesquad.cafe.servlet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codesquad.cafe.E2ETestBase;
import com.codesquad.cafe.SavedHttpResponse;
import com.codesquad.cafe.db.UserRepository;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserJoinServletTest extends E2ETestBase {

    private static UserRepository userRepository;

    private final String path = "/users/join";

    @BeforeAll
    static void beforeAll() {
        userRepository = (UserRepository) context.getServletContext().getAttribute("userRepository");
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 페이지를 반환한다.")
    void testDoGetUserJoinForm() throws IOException {
        //when
        SavedHttpResponse response = get(path);
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertTrue(html.contains("사용자 아이디"));
        assertTrue(html.contains("비밀번호"));
        assertTrue(html.contains("이름"));
        assertTrue(html.contains("이메일"));
    }

    @Test
    @DisplayName("회원가입에 성공하면 유저 리스트로 리다이렉트한다.")
    void testDoPostCreateUser() throws IOException, URISyntaxException {
        //when
        HttpResponse response = post(path,
                "username=woowa&password=1234&name=woowa&email=woowa24%40gmail.com");

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertEquals("/users", response.getFirstHeader("Location").getValue());
    }

    @Test
    @DisplayName("회원가입에 실패하면 예외를 던진다.-패스워드 누락")
    void testDoPostFailCreateUserWhenPasswordAbsent() throws IOException, URISyntaxException {
        HttpResponse response = post(path, "username=woowa&name=woowa&email=woowa24%40gmail.com");
    }

    @Test
    @DisplayName("유저 생성 실패 - 중복된 아이디입니다.")
    void testDoPostFailCreateUserWhenUsernameDuplicate() throws IOException, URISyntaxException {
        //when
        post("/users/join", "username=woowa&password=1234&name=woowa&email=woowa24%40gmail.com");
        HttpResponse response = post(path, "username=woowa&password=1234&name=woowa&email=woowa24%40gmail.com");

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getEntity().getContentType().getValue());
        String html = EntityUtils.toString(response.getEntity());
        assertTrue(html.contains("중복된 아이디"));
    }
}
