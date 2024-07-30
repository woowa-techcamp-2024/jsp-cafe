package org.example.jspcafe.post.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentResponseTest {

    @DisplayName("CommentResponse 인스턴스 생성 테스트")
    @Test
    void create() {
        // given
        Long commentId = 1L;
        Long userId = 1L;
        Long postId = 1L;
        String nickname = "nickname";
        String content = "content";
        LocalDateTime createdAt = LocalDateTime.of(2021, 8, 1, 0, 0, 0);

        // when
        CommentResponse commentResponse = new CommentResponse(commentId, userId, postId, nickname, content, createdAt);

        // then
        assertThat(commentResponse).isNotNull()
                .extracting("commentId", "userId", "postId", "nickname", "content", "createdAt")
                .containsExactly(commentId, userId, postId, nickname, content, createdAt);
    }

}