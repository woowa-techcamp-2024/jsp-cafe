package org.example.jspcafe.post.service;

import org.example.jspcafe.post.repository.JdbcPostRepository;
import org.example.jspcafe.post.request.PostCreateRequest;
import org.example.jspcafe.user.repository.JdbcUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class PostServiceTest {

    private PostService postService;
    private JdbcPostRepository postRepository;
    private JdbcUserRepository userRepository;

    @BeforeEach
    void setUp() {
        postRepository = new JdbcPostRepository();
        userRepository = new JdbcUserRepository();
        postService = new PostService(postRepository, userRepository);
    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

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

}