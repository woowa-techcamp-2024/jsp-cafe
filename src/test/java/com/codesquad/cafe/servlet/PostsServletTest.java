package com.codesquad.cafe.servlet;

import static com.codesquad.cafe.TestUtil.assertErrorPageResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codesquad.cafe.E2ETestBase;
import com.codesquad.cafe.SavedHttpResponse;
import com.codesquad.cafe.db.PostRepository;
import com.codesquad.cafe.db.UserRepository;
import com.codesquad.cafe.model.Post;
import com.codesquad.cafe.model.User;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostServletTest extends E2ETestBase {

    private static PostRepository postRepository;

    private static UserRepository userRepository;

    private static User user;

    private static Post post;

    private final String path = "/posts/";


    @BeforeAll
    static void beforeAll() {
        postRepository = (PostRepository) context.getServletContext().getAttribute("postRepository");
        userRepository = (UserRepository) context.getServletContext().getAttribute("userRepository");
        user = userRepository.save(User.of("javajigi", "1234", "박재성", "test@gmail.com"));
        post = postRepository.save(Post.of(user.getId(), "title1", "content1", null));
    }

    @AfterAll
    static void afterAll() {
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시판 상세보기 페이지를 반환한다.")
    void testDoGetUserProfile() throws IOException, URISyntaxException {
        //when
        SavedHttpResponse response = get(getPath(post.getId()));
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
    @DisplayName("없는 포스트 조회 요청시 404 응답을 반환한다.")
    void testDoGetPostCreateForm() throws IOException {
        //when
        SavedHttpResponse response = get(getPath(1000));

        //then
        assertErrorPageResponse(response, 404);
    }

    @Test
    @DisplayName("POST 요청시 405 응답을 반환한다.")
    void testDoPostReturn405() throws IOException, URISyntaxException {
        //when
        HttpResponse response = post(getPath(1), "");

        //then
        assertErrorPageResponse(response, 405);
    }

    private String getPath(long id) {
        return path + id;
    }
}
