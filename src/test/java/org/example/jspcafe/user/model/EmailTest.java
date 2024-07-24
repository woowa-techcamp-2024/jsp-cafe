package org.example.jspcafe.user.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @DisplayName("정상적으로 Email 객체를 생성하는 테스트")
    @Test
    void create() {
        // given
        String email = "example@gmail.com";

        // when
        Email result = new Email(email);

        // then
        assertThat(result).isNotNull()
                .extracting("value")
                .isEqualTo(email);
    }

    @DisplayName("Email 객체 생성 시, null이 들어오면 예외가 발생하는 테스트")
    @Test
    void create_Exception_NULL() {
        // given
        String email = null;

        // when & then
        assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email은 null이거나 빈 문자열일 수 없습니다.");
    }

    @DisplayName("Email 객체 생성 시, 빈 문자열이 들어오면 예외가 발생하는 테스트")
    @Test
    void create_Exception_Empty() {
        // given
        String email = "";

        // when & then
        assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email은 null이거나 빈 문자열일 수 없습니다.");
    }

    @DisplayName("Email 객체 생성 시, @가 없으면 예외가 발생하는 테스트")
    @Test
    void create_Exception_NoAt() {
        // given
        String email = "examplegmail.com";

        // when & then
        assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email은 @를 포함해야 합니다.");
    }


}