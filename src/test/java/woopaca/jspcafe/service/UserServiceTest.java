package woopaca.jspcafe.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import woopaca.jspcafe.fixture.TestRepositoryFixture;
import woopaca.jspcafe.servlet.dto.SignUpRequest;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceTest {

    UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(TestRepositoryFixture.testUserRepository());
    }

    @Nested
    class sign_up_메서드는 {

        @Nested
        class 유효한_회원가입_요청 {

            @Test
            void 정상적으로_회원가입에_성공한다() {
                SignUpRequest signUpRequest = new SignUpRequest("new", "new", "new");
                userService.signUp(signUpRequest);
                assertThatNoException().isThrownBy(() -> userService.signUp(signUpRequest));
            }
        }

        @Nested
        class 유효하지_않은_회원가입_요청 {

            @Test
            void 이메일이_비어있으면_예외가_발생한다() {
                SignUpRequest signUpRequest = new SignUpRequest("", "new", "new");
                assertThatThrownBy(() -> userService.signUp(signUpRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 이메일은 비어있을 수 없습니다.");
            }

            @Test
            void 닉네임이_비어있으면_예외가_발생한다() {
                SignUpRequest signUpRequest = new SignUpRequest("new", "", "new");
                assertThatThrownBy(() -> userService.signUp(signUpRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 닉네임은 비어있을 수 없습니다.");
            }

            @Test
            void 비밀번호가_비어있으면_예외가_발생한다() {
                SignUpRequest signUpRequest = new SignUpRequest("new", "new", "");
                assertThatThrownBy(() -> userService.signUp(signUpRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 비밀번호는 비어있을 수 없습니다.");
            }

            @Test
            void 이미_사용중인_이메일이면_예외가_발생한다() {
                SignUpRequest signUpRequest = new SignUpRequest("test@email.com", "new", "new");
                assertThatThrownBy(() -> userService.signUp(signUpRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 이미 사용 중인 이메일입니다.");
            }

            @Test
            void 이미_사용중인_닉네임이면_예외가_발생한다() {
                SignUpRequest signUpRequest = new SignUpRequest("new", "test", "new");
                assertThatThrownBy(() -> userService.signUp(signUpRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 이미 사용 중인 닉네임입니다.");
            }
        }
    }
}