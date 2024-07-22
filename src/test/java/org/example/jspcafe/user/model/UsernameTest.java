package org.example.jspcafe.user.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsernameTest {

    @DisplayName("정상적으로 Username 객체를 생성하는 테스트")
    @Test
    void create() {
        // given
        String username = "test";

        // when
        Username result = new Username(username);

        // then
        assertThat(result).isNotNull()
                .extracting("value")
                .isEqualTo(username);
    }

    @DisplayName("Username 객체 생성 시, null이 들어오면 예외가 발생하는 테스트")
    @Test
    void create_Exception_NULL() {
        // given
        String username = null;

        // when & then
        assertThatThrownBy(() -> new Username(username))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username은 null이거나 빈 문자열일 수 없습니다.");
    }

    @DisplayName("Username 객체 생성 시, 빈 문자열이 들어오면 예외가 발생하는 테스트")
    @Test
    void create_Exception_Empty() {
        // given
        String username = "";

        // when & then
        assertThatThrownBy(() -> new Username(username))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username은 null이거나 빈 문자열일 수 없습니다.");
    }

    @DisplayName("Username 객체 생성 시, 3자 미만이면 예외가 발생하는 테스트")
    @Test
    void create_Exception_Length_Min() {
        // given
        String username = "aa";

        // when & then
        assertThatThrownBy(() -> new Username(username))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username은 3자 이상 15자 이하여야 합니다.");
    }

    @DisplayName("Username 객체 생성 시, 15자 초과이면 예외가 발생하는 테스트")
    @Test
    void create_Exception_Length_Max() {
        // given
        String username = "aaaaaaaaaaaaaaaa";

        // when & then
        assertThatThrownBy(() -> new Username(username))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username은 3자 이상 15자 이하여야 합니다.");
    }


}