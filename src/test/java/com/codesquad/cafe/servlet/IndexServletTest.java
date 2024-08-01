package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.TestUtil.assertErrorPageResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codesquad.cafe.E2ETestBase;
import com.codesquad.cafe.SavedHttpResponse;
import com.codesquad.cafe.TestUtil;
import com.codesquad.cafe.db.PostRepository;
import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.db.entity.Post;
import com.codesquad.cafe.db.entity.User;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class IndexServletTest extends E2ETestBase {

    private static UserRepository userRepository;

    private static PostRepository postRepository;

    private static final String path = "/";

    private User user;

    private List<Post> posts;

    @BeforeAll
    static void beforeAll() throws Exception {
        userRepository = (UserRepository) context.getServletContext().getAttribute("userRepository");
        postRepository = (PostRepository) context.getServletContext().getAttribute("postRepository");
    }

    @BeforeEach
    void setUp() {
        posts = new ArrayList<>();
        user = userRepository.save(User.of("woowa", "1234", "김수현", "woowa@gmail.com"));
        posts.add(postRepository.save(Post.of(user.getId(), "title1", "content1", null)));
        posts.add(postRepository.save(Post.of(user.getId(), "title2", "content2", null)));
        posts.add(postRepository.save(Post.of(user.getId(), "title3", "content3", null)));
        posts.add(postRepository.save(Post.of(user.getId(), "title4", "content4", null)));
    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("기본 페이지를 반환한다. - 로그인 X")
    void testDoGetUnauthenticated() throws IOException {
        //when
        SavedHttpResponse response = get(path);
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());

        assertTrue(html.contains("사용자 목록"));
        assertTrue(html.contains("로그인"));
        assertTrue(html.contains("회원가입"));
        assertTrue(html.contains("전체 글 " + posts.size()));
    }

    @Test
    @DisplayName("기본 페이지를 반환한다. - 로그인 O")
    void testDoGetAuthenticated() throws IOException {
        //when
        HttpResponse loginResponse = post("/login",
                "username=" + user.getUsername() + "&password=" + user.getPassword());
        String sessionId = TestUtil.getSessionIdFromSetCookieHeader(
                loginResponse.getFirstHeader("Set-Cookie").getValue());
        SavedHttpResponse response = get(path, sessionId);
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());

        assertTrue(html.contains("사용자 목록"));
        assertTrue(html.contains("Profile"));
        assertFalse(html.contains("로그인"));
        assertFalse(html.contains("회원가입"));
        assertTrue(html.contains("로그아웃"));
        assertTrue(html.contains("개인정보수정"));

        assertTrue(html.contains("전체 글 " + posts.size()));
    }

    @DisplayName("page parameter에 맞는 게시글을 반환한다.")
    @ParameterizedTest
    @MethodSource
    void testDoGetPageParam(String pathWithQueryString, List<String> expectedPostsTitle, List<String> excludePosts)
            throws IOException {
        //when
        SavedHttpResponse response = get(pathWithQueryString);
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertTrue(html.contains("로그인"));
        assertTrue(html.contains("회원가입"));
        assertTrue(html.contains("전체 글 " + posts.size()));

        expectedPostsTitle.forEach(title -> {
            assertTrue(html.contains(title));
        });
        excludePosts.forEach(title -> {
            assertTrue(!html.contains(title));
        });
    }

    public static Stream<Arguments> testDoGetPageParam() {
        return Stream.of(
                Arguments.of(path + "?pageNum=1&pageSize=2", List.of("title4", "title3"), List.of("title2", "title1")),
                Arguments.of(path + "?pageNum=2&pageSize=2", List.of("title2", "title1"), List.of("title4", "title3"))
        );
    }

    @DisplayName("잘못된 pageParam 요청시 에러를 반환한다.")
    @Test
    void testDoGetInvalidPageParam() throws IOException {
        //when
        SavedHttpResponse response = get(path + "?pageNum=-1");

        //then
        assertEquals(400, response.getStatusLine().getStatusCode());
        assertTrue(response.getBody().contains("400 Bad Request"));
    }

    @Test
    @DisplayName("POST 요청시 405 응답을 반환한다.")
    void testDoPostReturn405() throws IOException, URISyntaxException {
        //when
        HttpResponse response = post(path, "");

        //then
        assertErrorPageResponse(response, 405);
    }

    @Test
    @DisplayName("매핑되지 않은 url 요청시 404 응답한다.")
    void testUnmappedUrlReturn404() throws IOException, URISyntaxException {
        //when
        HttpResponse response = post("/unknown", "");

        //then
        assertErrorPageResponse(response, 404);
    }
}
