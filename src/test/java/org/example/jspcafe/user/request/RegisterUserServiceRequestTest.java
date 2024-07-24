package org.example.jspcafe.user.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RegisterUserServiceRequestTest {

    @DisplayName("RegisterUserServiceRequest 생성")
    @Test
    void create() {
        // given
        String nickname = "nickname";
        String email = "email@example.com";
        String password = "password";

        // when
        RegisterUserServiceRequest request = new RegisterUserServiceRequest(nickname, email, password);

        // then
        assertThat(request)
                .extracting("nickname", "email", "password")
                .containsExactly(nickname, email, password);
    }

}