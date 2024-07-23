package org.example.jspcafe.user.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest {

    @DisplayName("UserResponse 생성")
    @Test
    void create() {
        // given
        String nickname = "nickname";
        String email = "email@example.com";

        // when
        UserResponse response = new UserResponse(nickname, email);

        // then
        assertThat(response)
                .extracting("nickname", "email")
                .containsExactly(nickname, email);
    }

}