package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.TestUtil.assertErrorPageResponse;
import static com.codesquad.cafe.TestUtil.getSessionIdFromSetCookieHeader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codesquad.cafe.E2ETestBase;
import com.codesquad.cafe.SavedHttpResponse;
import com.codesquad.cafe.db.PostRepository;
import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.db.entity.User;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostCreateServletTest extends E2ETestBase {

    private static PostRepository postRepository;

    private static UserRepository userRepository;

    private User user;

    private String sessionId;

    private final String path = "/posts/create";


    @BeforeAll
    static void beforeAll() {
        postRepository = (PostRepository) context.getServletContext().getAttribute("postRepository");
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
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterAll
    static void afterAll() {
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("글쓰기 페이지를 반환한다.")
    void testDoGetPostCreateForm() throws IOException {
        //when
        SavedHttpResponse response = get(path, sessionId);
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertTrue(html.contains("authorId"));
        assertTrue(html.contains("title"));
        assertTrue(html.contains("content"));
    }

    @Test
    @DisplayName("세션 쿠키 없이 글쓰기 페이지를 요청하면 로그인 페이지로 리다이렉트한다.")
    void testDoGetUnknownUser() throws IOException {
        //when
        SavedHttpResponse response = get(path);

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertEquals("/login", response.getFirstHeader("Location"));
    }

    @Test
    @DisplayName("글쓰기에 성공하면 홈으로로 리다이렉트한다.")
    void testDoPostCreateUser() throws IOException {
        //when
        HttpResponse response = post(path,
                "authorId=" + user.getId() + "&title=title&content=content",
                sessionId);

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertEquals("/", response.getFirstHeader("Location").getValue());
    }

    @Test
    @DisplayName("세션 쿠키 없이 글쓰기 요청시 로그인 페이지로 리다이렉트한다.")
    void testDoPostCreatePostWithoutSessionCookie() throws IOException {
        //when
        HttpResponse response = post(path,
                "authorId=" + user.getId() + "&title=title&content=content");

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertEquals("/login", response.getFirstHeader("Location").getValue());
    }

    @Test
    @DisplayName("세션 정보와 authorId가 다르면 401 응답한다.")
    void testDoPostFailUnknownUserId() throws IOException {
        //when
        HttpResponse response = post(path,
                "authorId=" + 1000 + "&title=title&content=content",
                sessionId);

        //then
        assertErrorPageResponse(response, 401);
    }

    @Test
    @DisplayName("userId로 숫자가 아닌 값을 요청하면 400 응답한다.")
    void testDoPostFailStringUserId() throws IOException {
        //when
        HttpResponse response = post(path,
                "authorId=author&title=title&content=content",
                sessionId);

        //then
        assertErrorPageResponse(response, 400);
    }

    @Test
    @DisplayName("누락된 필드로 요청하면 400 응답한다.")
    void testDoPostFailAbsentField() throws IOException {
        //when
        HttpResponse response = post(path,
                "authorId=" + user.getId() + "&content=content",
                sessionId);

        //then
        assertErrorPageResponse(response, 400);
    }

    @Test
    @DisplayName("빈 필드로 요청하면 400 응답한다.")
    void testDoPostFailEmptyField() throws IOException {
        //when
        HttpResponse response = post(path,
                "authorId=" + user.getId() + "&title=&content=content",
                sessionId);

        //then
        assertErrorPageResponse(response, 400);
    }

}
