package org.example.jspcafe.user.service;

import org.example.jspcafe.user.repository.InMemoryUserRepository;
import org.example.jspcafe.user.repository.JdbcUserRepository;
import org.example.jspcafe.user.request.RegisterUserServiceRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class SignupServiceTest {

    private SignupService signupService;
    private JdbcUserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new JdbcUserRepository();
        signupService = new SignupService(userRepository);
    }

    @AfterEach
    void tearDown() {
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