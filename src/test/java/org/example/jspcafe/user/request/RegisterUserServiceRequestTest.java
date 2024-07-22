package org.example.jspcafe.user.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterUserServiceRequestTest {
    @DisplayName("request를 생성할 수 있다")
    @Test
    void createRequest() {
        // given
        String nickname = "nickname";
        String email = "email@example.com";
        String password = "password";

        // when
        RegisterUserServiceRequest request = new RegisterUserServiceRequest(nickname, email, password);

        // then
        assertThat(request).isNotNull()
                .extracting(RegisterUserServiceRequest::nickname, RegisterUserServiceRequest::email, RegisterUserServiceRequest::password)
                .containsExactly(nickname, email, password);
    }

}