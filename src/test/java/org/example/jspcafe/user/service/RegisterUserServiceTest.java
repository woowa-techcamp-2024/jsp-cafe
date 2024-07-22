package org.example.jspcafe.user.service;

import org.example.jspcafe.Repository;
import org.example.jspcafe.user.model.User;
import org.example.jspcafe.user.repository.InMemoryUserRepository;
import org.example.jspcafe.user.request.RegisterUserServiceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class RegisterUserServiceTest {

    private RegisterUserService registerUserService;
    private Repository<User> userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        registerUserService = new RegisterUserService(userRepository);
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
        assertThatCode(() -> registerUserService.registerUser(request))
                .doesNotThrowAnyException();
    }
}