package org.example.jspcafe.post.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContentTest {

    @DisplayName("Content를 생성할 수 있다.")
    @Test
    void createContent() {
        // given
        String contentStr = "content";

        // when
        Content content = new Content(contentStr);

        // then
        assertThat(content)
                .extracting("value")
                .isEqualTo(contentStr);
    }

    @DisplayName("빈 문자열로 Content를 생성하면 예외가 발생한다.")
    @Test
    void createContentWithEmptyString() {
        // given
        String contentStr = "";

        // when & then
        assertThatThrownBy(() -> new Content(contentStr))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("내용이 없습니다.");
    }

    @DisplayName("null로 Content를 생성하면 예외가 발생한다.")
    @Test
    void createContentWithNull() {
        // given
        String contentStr = null;

        // when & then
        assertThatThrownBy(() -> new Content(contentStr))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("내용이 없습니다.");
    }

    @DisplayName("500자 이상 문자열로 Content를 생성하면 예외가 발생한다.")
    @Test
    void createContentWithOver500Length() {
        // given
        String contentStr = "a".repeat(501);

        // when & then
        assertThatThrownBy(() -> new Content(contentStr))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("내용은 500자 이하여야 합니다.");
    }

    @DisplayName("500자의 문자열로 Content를 생성할 수 있다.")
    @Test
    void createContentWith500Length() {
        // given
        String contentStr = "a".repeat(500);

        // when
        Content content = new Content(contentStr);

        // then
        assertThat(content)
                .extracting("value")
                .isEqualTo(contentStr);
    }
}
