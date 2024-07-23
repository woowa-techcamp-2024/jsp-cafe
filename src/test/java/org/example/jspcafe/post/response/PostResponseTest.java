package org.example.jspcafe.post.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PostResponseTest {

    @DisplayName("PostResponse 생성 테스트")
    @Test
    void createPostResponse() {
        // given
        Long postId = 1L;
        String nickname = "nickname";
        String title = "title";
        String content = "content";
        LocalDateTime createdAt = LocalDateTime.of(2021, 1, 1, 0, 0);

        // when
        PostResponse postResponse = new PostResponse(postId, nickname, title, content, createdAt);

        // then
        assertThat(postResponse)
                .extracting("postId", "nickname", "title", "content", "createdAt")
                .containsExactly(postId, nickname, title, content, createdAt);
    }

}