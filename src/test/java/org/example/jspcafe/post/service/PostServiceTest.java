package org.example.jspcafe.post.service;

import org.example.jspcafe.AbstractRepositoryTestSupport;
import org.example.jspcafe.post.repository.JdbcPostRepository;
import org.example.jspcafe.post.request.PostCreateRequest;
import org.example.jspcafe.user.repository.JdbcUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

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

    @Override
    protected void deleteAllInBatch() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }
}