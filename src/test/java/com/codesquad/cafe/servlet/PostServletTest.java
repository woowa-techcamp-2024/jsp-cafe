package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.TestUtil.assertErrorPageResponse;
import static com.codesquad.cafe.TestUtil.getSessionIdFromSetCookieHeader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codesquad.cafe.E2ETestBase;
import com.codesquad.cafe.SavedHttpResponse;
import com.codesquad.cafe.db.PostRepository;
import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.db.domain.Post;
import com.codesquad.cafe.db.domain.User;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostServletTest extends E2ETestBase {

    private static PostRepository postRepository;

    private static UserRepository userRepository;

    private final String path = "/posts/";

    private User user;

    private Post post;

    private String sessionId;

    @BeforeAll
    static void beforeAll() {
        postRepository = (PostRepository) context.getServletContext().getAttribute("postRepository");
        userRepository = (UserRepository) context.getServletContext().getAttribute("userRepository");
    }

    @BeforeEach
    void setUp() throws IOException {
        user = userRepository.save(User.of("javajigi", "1234", "박재성", "test@gmail.com"));
        post = postRepository.save(Post.of(user.getId(), "title1", "content1", null));
        sessionId = getSessionIdFromSetCookieHeader(
                post("/login", "username=javajigi&password=1234").getFirstHeader("Set-Cookie").getValue());
    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 상세보기 페이지를 반환한다.")
    void testDoGetPost() throws IOException, URISyntaxException {
        //when
        SavedHttpResponse response = get(getPath(post.getId()), sessionId);
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertTrue(html.contains(post.getTitle()));
        assertTrue(html.contains(user.getUsername()));
        assertTrue(html.contains("/users/" + user.getId()));
        assertTrue(html.contains("/posts/" + post.getId()));
        assertTrue(html.contains(post.getContent()));
        assertTrue(html.contains("수정"));
        assertTrue(html.contains("삭제"));
    }

    @Test
    @DisplayName("글쓰기 수정 폼을 반환한다.")
    void testDoGetPostEditForm() throws IOException, URISyntaxException {
        //when
        SavedHttpResponse response = get(getPath(post.getId()) + "/edit", sessionId);
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertTrue(html.contains(post.getTitle()));
        assertTrue(html.contains(post.getContent()));
        assertTrue(html.contains(post.getAuthorId().toString()));
        assertTrue(html.contains("수정하기"));
    }

    @Test
    @DisplayName("권한이 없는 글의 수정폼을 요청하면 403을 반환한다.")
    void testDoGetPostEditFormUnauthorized() throws IOException, URISyntaxException {
        //given
        User otherUser = userRepository.save(User.of("wow", "1234", "박재성", "test@gmail.com"));
        Post otherPost = postRepository.save(Post.of(otherUser.getId(), "title1", "content1", null));

        //when
        SavedHttpResponse response = get(getPath(otherPost.getId()) + "/edit", sessionId);
        String html = response.getBody();

        //then
        assertErrorPageResponse(response, 403);
    }

    @Test
    @DisplayName("없는 포스트 조회 요청시 404 응답을 반환한다.")
    void testDoGetPostNotFound() throws IOException {
        //when
        SavedHttpResponse response = get(getPath(1000));

        //then
        assertErrorPageResponse(response, 404);
    }

    @Test
    @DisplayName("게시글 리스트 조회 요청시 홈으로 리다이렉트한다.")
    void testDoGetPosts() throws IOException, URISyntaxException {
        //when
        SavedHttpResponse response = get("/posts", sessionId);

        //then
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertEquals("/", response.getFirstHeader("Location"));
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
    @DisplayName("세션 정보와 authorId가 다르면 403 응답한다.")
    void testDoPostFailUnknownUserId() throws IOException {
        //when
        HttpResponse response = post(path,
                "authorId=" + 1000 + "&title=title&content=content",
                sessionId);

        //then
        assertErrorPageResponse(response, 403);
    }

    @Test
    @DisplayName("userId로 숫자가 아닌 값을 요청하면 404 응답한다.")
    void testDoGetFailStringUserId() throws IOException {
        //when
        SavedHttpResponse response = get("/posts/ksdjf");

        //then
        assertErrorPageResponse(response, 404);
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


    private String getPath(long id) {
        return path + id;
    }
}
