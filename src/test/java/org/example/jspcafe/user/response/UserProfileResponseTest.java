package org.example.jspcafe.user.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserProfileResponseTest {

    @DisplayName("UserProfileResponse 생성")
    @Test
    void create() {
        // given
        String nickname = "nickname";
        String email = "email@example.com";
        LocalDateTime createdAt = LocalDateTime.of(2021, 1, 1, 0, 0, 0, 0);

        // when
        UserProfileResponse response = new UserProfileResponse(nickname, email, createdAt);

        // then
        assertThat(response)
                .extracting("nickname", "email", "createdAt")
                .containsExactly(nickname, email, createdAt);
    }

}