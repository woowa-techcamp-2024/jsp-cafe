package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.TestUtil.assertErrorPageResponse;
import static com.codesquad.cafe.TestUtil.getSessionIdFromSetCookieHeader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codesquad.cafe.E2ETestBase;
import com.codesquad.cafe.SavedHttpResponse;
import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.db.domain.User;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsersServletTest extends E2ETestBase {

    private static UserRepository userRepository;

    private final String path = "/users";

    private User user;

    private String sessionId;


    @BeforeAll
    static void beforeAll() {
        userRepository = (UserRepository) context.getServletContext().getAttribute("userRepository");
    }

    @BeforeEach
    void setUp() throws IOException {
        user = userRepository.save(User.of("javajigi", "1234", "박재성", "test@gmail.com"));
        sessionId = getSessionIdFromSetCookieHeader(
                post("/login", "username=javajigi&password=1234").getFirstHeader("Set-Cookie").getValue());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저 목록 페이지를 반환한다.")
    void testDoGetUserList() throws IOException, URISyntaxException {
        //when
        SavedHttpResponse response = get(path, sessionId);
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertTrue(html.contains("전체 사용자 1명"));
        assertTrue(html.contains("<a href=\"/users/"));
        assertTrue(html.contains(user.getUsername()));
    }

    @Test
    @DisplayName("인증 없이 유저 리스트를 요청시 로그인 페이지로 리다이렉트한다.")
    void testDoGetUserListUnauthorized() throws IOException, URISyntaxException {
        //when
        SavedHttpResponse response = get(path);
        String html = response.getBody();

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertEquals("/login", response.getFirstHeader("Location"));
    }

    @Test
    @DisplayName("유저 프로필을 반환한다.")
    void testDoGetUserProfile() throws IOException, URISyntaxException {
        //when
        SavedHttpResponse response = get(getPath(user.getId()), sessionId);
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertTrue(html.contains(user.getUsername()));
    }

    @Test
    @DisplayName("인증 없이 유저 프로필을 요청시 로그인페이지로 리다이렉트한다.")
    void testDoGetUserProfileUnauthorized() throws IOException, URISyntaxException {
        //when
        SavedHttpResponse response = get(getPath(user.getId()));
        String html = response.getBody();

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertEquals("/login", response.getFirstHeader("Location"));
    }

    @Test
    @DisplayName("POST 요청시 405 응답을 반환한다.")
    void testDoPostReturn405() throws IOException, URISyntaxException {
        //when
        HttpResponse response = post(path, "");

        //then
        assertErrorPageResponse(response, 405);
    }

    private String getPath(Long id) {
        return path + "/" + id;
    }
}
