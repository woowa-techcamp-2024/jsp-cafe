package woowa.camp.jspcafe.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowa.camp.jspcafe.domain.exception.UserException;
import woowa.camp.jspcafe.repository.InMemoryUserRepository;
import woowa.camp.jspcafe.repository.UserRepository;
import woowa.camp.jspcafe.service.dto.RegistrationRequest;

class UserServiceTest {

    UserRepository userRepository = new InMemoryUserRepository();
    UserService userService = new UserService(userRepository);

    @Test
    @DisplayName("[Success] 아이디, 비밀번호, 이름, 이메일 정보가 모두 있어야 회원가입을 성공한다")
    void test() {
        RegistrationRequest registrationRequest = new RegistrationRequest("아이디", "비밀번호", "이름", "email.com");
        assertThatCode(() -> userService.registration(registrationRequest))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("[Exception] 아이디 정보가 없으면 회원가입을 실패한다")
    void test2() {
        RegistrationRequest registrationRequest = new RegistrationRequest(null, "비밀번호", "이름", "email.com");
        assertThatThrownBy(() -> userService.registration(registrationRequest))
                .isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("[Exception] 아이디 정보가 빈값이면 회원가입을 실패한다")
    void test6() {
        RegistrationRequest registrationRequest = new RegistrationRequest("", "비밀번호", "이름", "email.com");
        assertThatThrownBy(() -> userService.registration(registrationRequest))
                .isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("[Exception] 비밀번호 정보가 없으면 회원가입을 실패한다")
    void test3() {
        RegistrationRequest registrationRequest = new RegistrationRequest("아이디", null, "이름", "email.com");
        assertThatThrownBy(() -> userService.registration(registrationRequest))
                .isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("[Exception] 비밀번호 정보가 빈값이면 회원가입을 실패한다")
    void test7() {
        RegistrationRequest registrationRequest = new RegistrationRequest("아이디", "", "이름", "email.com");
        assertThatThrownBy(() -> userService.registration(registrationRequest))
                .isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("[Exception] 이름 정보가 없으면 회원가입을 실패한다")
    void test4() {
        RegistrationRequest registrationRequest = new RegistrationRequest("아이디", "비밀번호", null, "email.com");
        assertThatThrownBy(() -> userService.registration(registrationRequest))
                .isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("[Exception] 이름 정보가 빈값이면 회원가입을 실패한다")
    void test8() {
        RegistrationRequest registrationRequest = new RegistrationRequest("아이디", "비밀번호", "", "email.com");
        assertThatThrownBy(() -> userService.registration(registrationRequest))
                .isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("[Exception] 이메일 정보가 없으면 회원가입을 실패한다")
    void test5() {
        RegistrationRequest registrationRequest = new RegistrationRequest("아이디", "비밀번호", "이름", null);
        assertThatThrownBy(() -> userService.registration(registrationRequest))
                .isInstanceOf(UserException.class);
    }

    @Test
    @DisplayName("[Exception] 이메일 정보가 빈값이면 회원가입을 실패한다")
    void test9() {
        RegistrationRequest registrationRequest = new RegistrationRequest("아이디", "비밀번호", "이름", "");
        assertThatThrownBy(() -> userService.registration(registrationRequest))
                .isInstanceOf(UserException.class);
    }

}