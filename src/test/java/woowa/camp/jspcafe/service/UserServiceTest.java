package woowa.camp.jspcafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.domain.exception.UserException;
import woowa.camp.jspcafe.fixture.UserFixture;
import woowa.camp.jspcafe.repository.InMemoryUserRepository;
import woowa.camp.jspcafe.repository.UserRepository;
import woowa.camp.jspcafe.service.dto.RegistrationRequest;
import woowa.camp.jspcafe.service.dto.UserResponse;

class UserServiceTest {

    @Nested
    @DisplayName("Describe_회원가입 기능은")
    class RegistrationTest {

        UserRepository userRepository = new InMemoryUserRepository();
        UserService userService = new UserService(userRepository);

        @Test
        @DisplayName("[Success] 아이디, 비밀번호, 이름, 이메일 정보가 모두 있어야 회원가입을 성공한다")
        void registration() {
            RegistrationRequest registrationRequest = new RegistrationRequest("아이디", "비밀번호", "이름", "email.com");
            assertThatCode(() -> userService.registration(registrationRequest))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest(name = "[Exception] {0} 정보가 없으면 회원가입을 실패한다")
        @ValueSource(strings = {"아이디", "비밀번호", "이름", "이메일"})
        void registrationFailureWhenMissingField(String fieldName) {
            RegistrationRequest registrationRequest = createRequestWithMissingField(fieldName);
            assertThatThrownBy(() -> userService.registration(registrationRequest))
                    .isInstanceOf(UserException.class);
        }

        @ParameterizedTest(name = "[Exception] {0} 정보가 빈값이면 회원가입을 실패한다")
        @ValueSource(strings = {"아이디", "비밀번호", "이름", "이메일"})
        void registrationFailureWhenEmptyField(String fieldName) {
            RegistrationRequest registrationRequest = createRequestWithEmptyField(fieldName);
            assertThatThrownBy(() -> userService.registration(registrationRequest))
                    .isInstanceOf(UserException.class);
        }

        private RegistrationRequest createRequestWithMissingField(String fieldName) {
            return switch (fieldName) {
                case "아이디" -> new RegistrationRequest(null, "비밀번호", "이름", "email.com");
                case "비밀번호" -> new RegistrationRequest("아이디", null, "이름", "email.com");
                case "이름" -> new RegistrationRequest("아이디", "비밀번호", null, "email.com");
                case "이메일" -> new RegistrationRequest("아이디", "비밀번호", "이름", null);
                default -> throw new IllegalArgumentException("Unexpected field: " + fieldName);
            };
        }

        private RegistrationRequest createRequestWithEmptyField(String fieldName) {
            return switch (fieldName) {
                case "아이디" -> new RegistrationRequest("", "비밀번호", "이름", "email.com");
                case "비밀번호" -> new RegistrationRequest("아이디", "", "이름", "email.com");
                case "이름" -> new RegistrationRequest("아이디", "비밀번호", "", "email.com");
                case "이메일" -> new RegistrationRequest("아이디", "비밀번호", "이름", "");
                default -> throw new IllegalArgumentException("Unexpected field: " + fieldName);
            };
        }
    }

    @Nested
    @DisplayName("Describe_사용자 목록을 조회하는 기능은")
    class FindAllTest {

        UserRepository userRepository = new InMemoryUserRepository();
        UserService userService = new UserService(userRepository);

        @Test
        @DisplayName("[Success] 사용자 목록에 대한 UserResponse 리스트를 반환한다")
        void test() {
            User user1 = UserFixture.createUser1();
            User user2 = UserFixture.createUser2();
            userRepository.save(user1);
            userRepository.save(user2);
            List<UserResponse> expected = List.of(UserResponse.of(user1), UserResponse.of(user2));
            List<UserResponse> result = userService.findAll();

            assertThat(result).size().isEqualTo(2);
            assertThat(result).containsExactlyElementsOf(expected);
        }
    }

}