package org.example.jspcafe.comment.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentDeleteRequestTest {

    @DisplayName("생성 테스트")
    @Test
    void creation() {
        // given
        Long commentId = 1L;
        Long userId = 2L;

        // when
        CommentDeleteRequest request = new CommentDeleteRequest(commentId, userId);

        // then
        assertThat(request).isNotNull()
                .extracting("commentId", "userId")
                .contains(commentId, userId);
    }

}