package org.example.jspcafe.user.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @DisplayName("정상적으로 Password 객체를 생성하는 테스트")
    @Test
    void create() {
        // given
        String password = "test1234";

        // when
        Password result = new Password(password);

        // then
        assertThat(result).isNotNull()
                .extracting("value")
                .isEqualTo(password);
    }

    @DisplayName("Password 객체 생성 시, null이 들어오면 예외가 발생하는 테스트")
    @Test
    void create_Exception_NULL() {
        // given
        String password = null;

        // when & then
        assertThatThrownBy(() -> new Password(password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password는 null이거나 빈 문자열일 수 없습니다.");
    }

    @DisplayName("Password 객체 생성 시, 빈 문자열이 들어오면 예외가 발생하는 테스트")
    @Test
    void create_Exception_Empty() {
        // given
        String password = "";

        // when & then
        assertThatThrownBy(() -> new Password(password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password는 null이거나 빈 문자열일 수 없습니다.");
    }

    @DisplayName("Password 객체 생성 시, 8자 미만이면 예외가 발생하는 테스트")
    @Test
    void create_Exception_Length_Min() {
        // given
        String password = "test123";

        // when & then
        assertThatThrownBy(() -> new Password(password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password는 8자 이상 20자 이하여야 합니다.");
    }

    @DisplayName("Password 객체 생성 시, 20자 초과이면 예외가 발생하는 테스트")
    @Test
    void create_Exception_Length_Max() {
        // given
        String password = "test1234567890123456789";

        // when & then
        assertThatThrownBy(() -> new Password(password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password는 8자 이상 20자 이하여야 합니다.");
    }


    @DisplayName("Password 객체 생성 시, 20자인 경우 정상적으로 생성되는 테스트")
    @Test
    void create_Normal_Max() {
        // given
        String password = "test1234567890123456";

        // when
        Password result = new Password(password);

        // then
        assertThat(result).isNotNull()
                .extracting("value")
                .isEqualTo(password);
    }

    @DisplayName("Password 객체 생성 시, 8자인 경우 정상적으로 생성되는 테스트")
    @Test
    void create_Normal_Min() {
        // given
        String password = "test1234";

        // when
        Password result = new Password(password);

        // then
        assertThat(result).isNotNull()
                .extracting("value")
                .isEqualTo(password);
    }

}