package org.example.jspcafe.user.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginServiceResponseTest {

    @DisplayName("LoginServiceResponse 생성")
    @Test
    void create() {
        // given
        Long userId = 1L;
        String nickname = "nickname";

        // when
        LoginServiceResponse response = new LoginServiceResponse(userId, nickname);

        // then
        assertThat(response)
                .extracting("userId", "nickname")
                .containsExactly(userId, nickname);
    }

}