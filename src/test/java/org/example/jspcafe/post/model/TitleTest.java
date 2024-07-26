package org.example.jspcafe.post.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TitleTest {
    
    @DisplayName("Title를 생성할 수 있다.")
    @Test
    void createTitle() {
        // given
        String titleStr = "title";

        // when
        Title title = new Title(titleStr);

        // then
        assertThat(title)
                .extracting("value")
                .isEqualTo(titleStr);
    }

    @DisplayName("빈 문자열로 Title를 생성하면 예외가 발생한다.")
    @Test
    void createTitleWithEmptyString() {
        // given
        String titleStr = "";

        // when & then
        assertThatThrownBy(() -> new Title(titleStr))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목이 없습니다.");
    }

    @DisplayName("null로 title를 생성하면 예외가 발생한다.")
    @Test
    void createTitleWithNull() {
        // given
        String titleStr = null;

        // when & then
        assertThatThrownBy(() -> new Title(titleStr))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목이 없습니다.");
    }

    @DisplayName("30자 이상 문자열로 title를 생성하면 예외가 발생한다.")
    @Test
    void createTitleWithOver30Length() {
        // given
        String titleStr = "a".repeat(31);

        // when & then
        assertThatThrownBy(() -> new Title(titleStr))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목은 30자 이하여야 합니다.");
    }

    @DisplayName("30자의 문자열로 Title를 생성할 수 있다.")
    @Test
    void createTitleWith500Length() {
        // given
        String titleStr = "a".repeat(30);

        // when
        Title title = new Title(titleStr);

        // then
        assertThat(title)
                .extracting("value")
                .isEqualTo(titleStr);
    }
}