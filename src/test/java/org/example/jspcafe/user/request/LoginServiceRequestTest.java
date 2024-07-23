package org.example.jspcafe.user.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginServiceRequestTest {

    @DisplayName("LoginServiceRequest 생성")
    @Test
    void create() {
        // given
        String email = "email@example.com";
        String password = "password";

        // when
        LoginServiceRequest loginServiceRequest = new LoginServiceRequest(email, password);

        // then
        assertThat(loginServiceRequest)
                .extracting("email", "password")
                .containsExactly(email, password);

    }

}