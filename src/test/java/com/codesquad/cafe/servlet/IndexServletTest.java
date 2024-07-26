package com.codesquad.cafe.servlet;

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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class IndexServletTest extends E2ETestBase {

    private static UserRepository userRepository;

    private static PostRepository postRepository;

    private static final String path = "/";

    private static User user;

    private static List<Post> posts;

    @BeforeAll
    static void beforeAll() throws Exception {
        E2ETestBase.setUpClass();
        userRepository = (UserRepository) context.getServletContext().getAttribute("userRepository");
        postRepository = (PostRepository) context.getServletContext().getAttribute("postRepository");
        posts = new ArrayList<>();
        user = userRepository.save(User.of("woowa", "1234", "김수현", "woowa@gmail.com"));
        posts.add(postRepository.save(new Post(user.getId(), "title1", "content1", null)));
        posts.add(postRepository.save(new Post(user.getId(), "title2", "content2", null)));
        posts.add(postRepository.save(new Post(user.getId(), "title3", "content3", null)));
        posts.add(postRepository.save(new Post(user.getId(), "title4", "content4", null)));
    }

    @AfterAll
    static void afterAll() {
        userRepository.deleteAll();
        postRepository.deleteAll();
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

    @DisplayName("page parameter에 맞는 게시글을 반환한다.")
    @ParameterizedTest
    @MethodSource
    void testDoGetPageParam(String pathWithQueryString, List<Post> expectedPosts) throws IOException {
        //when
        SavedHttpResponse response = get(pathWithQueryString);
        String html = response.getBody();

        //then
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("text/html;charset=UTF-8", response.getContentType());
        assertTrue(html.contains("사용자 목록"));
        assertTrue(html.contains("로그인"));
        assertTrue(html.contains("회원가입"));
        assertTrue(html.contains("전체 글 " + posts.size()));

        expectedPosts.forEach(post -> {
            assertTrue(html.contains(post.getTitle()));
        });
    }

    public static Stream<Arguments> testDoGetPageParam() {
        return Stream.of(
                Arguments.of(path, posts.stream().toList()),
                Arguments.of(path + "?pageNum=1&pageSize=2", List.of(posts.get(3), posts.get(2))),
                Arguments.of(path + "?pageNum=2&pageSize=2", List.of(posts.get(1), posts.get(0))),
                Arguments.of(path + "?pageNum=2&pageSize=3", List.of())
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
        assertEquals(405, response.getStatusLine().getStatusCode());
        assertTrue(EntityUtils.toString(response.getEntity()).contains("405 Method Not Allowed"));
    }

}
