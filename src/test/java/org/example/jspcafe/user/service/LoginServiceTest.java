package org.example.jspcafe.user.service;

import org.example.jspcafe.user.model.User;
import org.example.jspcafe.user.repository.JdbcUserRepository;
import org.example.jspcafe.user.request.LoginServiceRequest;
import org.example.jspcafe.user.request.LoginServiceResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoginServiceTest {

    private JdbcUserRepository userRepository;
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        userRepository = new JdbcUserRepository();
        loginService = new LoginService(userRepository);

        // given: 사용자 저장
        userRepository.save(new User("nickname", "email@example.com", "password", LocalDateTime.now()));
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("로그인 성공 시 올바른 응답을 반환한다")
    void loginSuccess() {
        // given
        LoginServiceRequest request = new LoginServiceRequest("email@example.com", "password");

        // when
        LoginServiceResponse response = loginService.login(request);

        // then
        assertThat(response)
                .extracting(LoginServiceResponse::nickname)
                .isEqualTo("nickname");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 시 예외를 던진다")
    void loginWithNonExistentEmail() {
        // given
        LoginServiceRequest request = new LoginServiceRequest("nonexistent@example.com", "password");

        // when then
        assertThatThrownBy(() -> loginService.login(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 예외를 던진다")
    void loginWithIncorrectPassword() {
        // given
        LoginServiceRequest request = new LoginServiceRequest("email@example.com", "wrongpassword");

        // when then
        assertThatThrownBy(() -> loginService.login(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }
}
