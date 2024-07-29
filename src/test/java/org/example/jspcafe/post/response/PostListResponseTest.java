package org.example.jspcafe.post.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PostListResponseTest {

    @DisplayName("PostListResponse 생성 테스트")
    @Test
    void createPostListResponse() {
        // given
        List<PostResponse> postList = List.of(
                new PostResponse(1L, 1L, "nickname", "title", "content", null),
                new PostResponse(2L, 1L, "nickname", "title", "content", null)
        );
        int totalElements = postList.size();

        // when
        PostListResponse postListResponse = PostListResponse.of(totalElements, postList);

        // then
        assertThat(postListResponse)
                .extracting("totalElements", "count", "postList")
                .containsExactly(totalElements, postList.size(), postList);

    }

}