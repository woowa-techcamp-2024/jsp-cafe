package org.example.jspcafe.comment.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentModifyRequestTest {

    @DisplayName("생성 테스트")
    @Test
    void creation() {
        // given
        Long commentId = 1L;
        Long userId = 2L;
        String content = "content";

        // when
        CommentModifyRequest request = new CommentModifyRequest(commentId, userId, content);

        // then
        assertThat(request).isNotNull()
                .extracting("commentId", "userId", "content")
                .contains(commentId, userId, content);
    }

}