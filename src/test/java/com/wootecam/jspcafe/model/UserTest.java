package com.wootecam.jspcafe.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void 사용자의_정보중_하나라도_비어있다면_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> new User("userId", "", "name", "mail@mail.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원가입 시 모든 정보를 입력해야 합니다.");
    }

    @Test
    void 사용자의_정보중_하나라도_null이_입력되면_예외가_발생한다() {
        // expect
        assertThatThrownBy(() -> new User("userId", "password", "name", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원가입 시 모든 정보를 입력해야 합니다.");
    }
}
