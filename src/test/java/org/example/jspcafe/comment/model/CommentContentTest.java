package org.example.jspcafe.comment.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentContentTest {

    @DisplayName("정상적으로 CommentContent 객체를 생성한다.")
    @Test
    void createCommentContent() {
        // given
        String content = "댓글 내용";

        // when
        CommentContent commentContent = new CommentContent(content);

        // then
        assertThat(commentContent.getValue())
                .isEqualTo(content);
    }

    @DisplayName("댓글 내용이 200자 이하면 CommentContent 객체를 생성한다.")
    @Test
    void createCommentContentWith200() {
        // given
        String content = "a".repeat(200);

        // when
        CommentContent commentContent = new CommentContent(content);

        // then
        assertThat(commentContent.getValue())
                .isEqualTo(content);

    }

    @DisplayName("댓글 내용이 200자를 초과하면 IllegalArgumentException이 발생한다.")
    @Test
    void validateLength() {
        // given
        String content = "a".repeat(201);

        // when & then
        assertThatThrownBy(() -> new CommentContent(content))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글 내용은 200 이하여야 합니다.");

    }

    @DisplayName("댓글 내용이 null이면 IllegalArgumentException이 발생한다.")
    @Test
    void validateContentWithNull() {
        // given
        String content = null;

        // when & then
        assertThatThrownBy(() -> new CommentContent(content))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("내용이 없습니다.");

    }

    @DisplayName("댓글 내용이 비어있이면 IllegalArgumentException이 발생한다.")
    @Test
    void validateContentWithEmpty() {
        // given
        String content = "";

        // when & then
        assertThatThrownBy(() -> new CommentContent(content))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("내용이 없습니다.");

    }
}