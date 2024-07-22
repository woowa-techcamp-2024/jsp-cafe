package org.example.jspcafe.user.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NicknameTest {

    @DisplayName("정상적으로 Nickname 객체를 생성하는 테스트")
    @Test
    void create() {
        // given
        String nickname = "test";

        // when
        Nickname result = new Nickname(nickname);

        // then
        assertThat(result).isNotNull()
                .extracting("value")
                .isEqualTo(nickname);
    }

    @DisplayName("Nickname 객체 생성 시, null이 들어오면 예외가 발생하는 테스트")
    @Test
    void create_Exception_NULL() {
        // given
        String nickname = null;

        // when & then
        assertThatThrownBy(() -> new Nickname(nickname))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nickname은 null이거나 빈 문자열일 수 없습니다.");
    }

    @DisplayName("Nickname 객체 생성 시, 빈 문자열이 들어오면 예외가 발생하는 테스트")
    @Test
    void create_Exception_Empty() {
        // given
        String nickname = "";

        // when & then
        assertThatThrownBy(() -> new Nickname(nickname))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nickname은 null이거나 빈 문자열일 수 없습니다.");
    }

    @DisplayName("Nickname 객체 생성 시, 3자 미만이면 예외가 발생하는 테스트")
    @Test
    void create_Exception_Length_Min() {
        // given
        String nickname = "aa";

        // when & then
        assertThatThrownBy(() -> new Nickname(nickname))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nickname은 3자 이상 15자 이하여야 합니다.");
    }

    @DisplayName("Nickname 객체 생성 시, 15자 초과이면 예외가 발생하는 테스트")
    @Test
    void create_Exception_Length_Max() {
        // given
        String nickname = "aaaaaaaaaaaaaaaa";

        // when & then
        assertThatThrownBy(() -> new Nickname(nickname))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nickname은 3자 이상 15자 이하여야 합니다.");
    }


}