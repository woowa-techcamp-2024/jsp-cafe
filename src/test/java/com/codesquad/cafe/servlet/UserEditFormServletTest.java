package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.TestUtil.getSessionIdFromSetCookieHeader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codesquad.cafe.E2ETestBase;
import com.codesquad.cafe.SavedHttpResponse;
import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.db.domain.User;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserEditFormServletTest extends E2ETestBase {

    private static String path = "/me/edit";

    private static UserRepository userRepository;

    private User user;

    private String sessionId;

    @BeforeAll
    static void beforeAll() {
        userRepository = (UserRepository) context.getServletContext().getAttribute("userRepository");
    }

    @BeforeEach
    void setUp() throws IOException {
        user = userRepository.save(User.of("javajigi", "1234", "박재성", "park@gmail.com"));
        sessionId = getSessionIdFromSetCookieHeader(
                post("/login", "username=javajigi&password=1234").getFirstHeader("Set-Cookie").getValue());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저 수정 페이지를 반환한다.")
    void testDoGetUserProfile() throws IOException {
        //when
        SavedHttpResponse response = get(path, sessionId);
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertTrue(html.contains("userModify"));
        assertTrue(html.contains(user.getUsername()));
        assertTrue(html.contains(user.getEmail()));
    }

    @Test
    @DisplayName("세션 쿠키 없이 수정 페이지를 요청하면 로그인 페이지로 리다이렉트한다.")
    void testDoGetUnknownUser() throws IOException {
        //when
        SavedHttpResponse response = get(path);

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertEquals("/login", response.getFirstHeader("Location"));
    }

}
