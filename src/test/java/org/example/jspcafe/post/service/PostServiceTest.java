package org.example.jspcafe.post.service;

import org.example.jspcafe.AbstractRepositoryTestSupport;
import org.example.jspcafe.post.model.Post;
import org.example.jspcafe.post.repository.JdbcPostRepository;
import org.example.jspcafe.post.request.PostCreateRequest;
import org.example.jspcafe.post.response.PostListResponse;
import org.example.jspcafe.post.response.PostResponse;
import org.example.jspcafe.user.model.User;
import org.example.jspcafe.user.repository.JdbcUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

class PostServiceTest extends AbstractRepositoryTestSupport {

    private JdbcPostRepository postRepository = new JdbcPostRepository(super.connectionManager);
    private JdbcUserRepository userRepository = new JdbcUserRepository(super.connectionManager);
    private PostService postService = new PostService(postRepository, userRepository);

    @DisplayName("게시글을 생성할 수 있다.")
    @Test
    void createPost() {
        // given
        Long userId = 1L;
        String title = "title";
        String content = "content";

        PostCreateRequest request = new PostCreateRequest(userId, title, content);

        // when & then
        assertThatCode(() -> postService.createPost(request))
                .doesNotThrowAnyException();
    }

    @DisplayName("getPosts를 요청하면 page 번호와 size 만큼의 게시글을 조회할 수 있다.")
    @Test
    void getPosts() {
        // given
        User user1 = userRepository.save(new User("nickname1", "email1@example.com", "password", LocalDateTime.of(2021, 1, 1, 0, 0, 0)));
        User user2 = userRepository.save(new User("nickname2", "email2@example.com", "password", LocalDateTime.of(2021, 1, 1, 0, 0, 0)));

        String title = "title";
        String content = "content";

        PostCreateRequest request1 = new PostCreateRequest(user1.getUserId(), title, content);
        PostCreateRequest request2 = new PostCreateRequest(user2.getUserId(), title, content);
        postService.createPost(request1);
        postService.createPost(request2);

        int page = 1;
        int size = 2;


        // when
        PostListResponse posts = postService.getPosts(page, size);

        // then
        assertAll(
                () -> assertThat(posts).isNotNull()
                        .extracting("totalElements")
                        .isEqualTo(2),
                () -> assertThat(posts.postList()).hasSize(2)
                        .extracting("title", "content")
                        .containsExactly(
                                tuple("title", "content"),
                                tuple("title", "content")
                        )
        );
    }

    @DisplayName("getPost를 요청할 때 사용자가 존재하지 않으면 예외가 발생한다.")
    @Test
    void getPostWithInvalidUser() {
        // given
        String title = "title";
        String content = "content";
        Post post = postRepository.save(new Post(1L, title, content, LocalDateTime.of(2021, 1, 1, 0, 0, 0)));

        // when & then
        assertThatThrownBy(() -> postService.getPost(String.valueOf(post.getPostId())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @DisplayName("getPost를 요청할 때 게시글이 존재하지 않으면 예외가 발생한다.")
    @Test
    void getPostWithInvalidPost() {
        // when & then
        assertThatThrownBy(() -> postService.getPost("1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글을 찾을 수 없습니다.");
    }


    @DisplayName("getPost를 요청하면 게시글을 조회할 수 있다.")
    @Test
    void getPost() {
        // given
        User user1 = userRepository.save(new User("nickname1", "email1@example.com", "password", LocalDateTime.of(2021, 1, 1, 0, 0, 0)));

        Post savedPost = postRepository.save(new Post(user1.getUserId(), "title", "content", LocalDateTime.of(2021, 1, 1, 0, 0, 0)));


        // when
        PostResponse response = postService.getPost(String.valueOf(savedPost.getPostId()));


        // then
        assertThat(response).isNotNull()
                .extracting("title", "content")
                .containsExactly("title", "content");

    }


    @Override
    protected void deleteAllInBatch() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }
}