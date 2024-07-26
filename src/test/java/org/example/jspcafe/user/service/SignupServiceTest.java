package org.example.jspcafe.user.service;

import org.example.jspcafe.AbstractRepositoryTestSupport;
import org.example.jspcafe.H2DatabaseConnectionManager;
import org.example.jspcafe.user.repository.JdbcUserRepository;
import org.example.jspcafe.user.request.RegisterUserServiceRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class SignupServiceTest extends AbstractRepositoryTestSupport {

    private JdbcUserRepository userRepository = new JdbcUserRepository(super.connectionManager);
    private SignupService signupService = new SignupService(userRepository);

    @Override
    protected void deleteAllInBatch() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("사용자 등록 테스트")
    @Test
    void registerUser() {
        // given
        String nickname = "nickname";
        String email = "email@example.com";
        String password = "password";

        final RegisterUserServiceRequest request = new RegisterUserServiceRequest(nickname, email, password);

        // when & then
        assertThatCode(() -> signupService.registerUser(request))
                .doesNotThrowAnyException();
    }
}