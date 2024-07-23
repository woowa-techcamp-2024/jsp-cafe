package org.example.jspcafe.post.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostCreateRequestTest {

    @DisplayName("PostCreateRequest 생성")
    @Test
    void create() {
        // given
        Long userId = 1L;
        String title = "title";
        String content = "content";

        // when
        PostCreateRequest request = new PostCreateRequest(userId, title, content);

        // then
        assertThat(request)
                .extracting("userId", "title", "content")
                .containsExactly(userId, title, content);

    }

}