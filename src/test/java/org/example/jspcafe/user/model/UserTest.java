package org.example.jspcafe.user.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @DisplayName("사용자 생성 테스트")
    @Test
    void createUser() {
        // given
        String nickname = "nickname";
        String email = "example@email.com";
        String password = "password";

        // when
        User user = new User(nickname, email, password);

        // then
        assertThat(user).isNotNull()
                .extracting(u -> u.getNickname().getValue(), u -> u.getEmail().getValue(), u -> u.getPassword().getValue())
                .containsExactly(nickname, email, password);
    }

}