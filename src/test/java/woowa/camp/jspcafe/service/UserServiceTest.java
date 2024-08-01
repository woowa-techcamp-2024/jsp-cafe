package woowa.camp.jspcafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.domain.exception.UserException;
import woowa.camp.jspcafe.fixture.UserFixture;
import woowa.camp.jspcafe.infra.DatabaseConnector;
import woowa.camp.jspcafe.infra.time.DateTimeProvider;
import woowa.camp.jspcafe.repository.UserDBSetupExtension;
import woowa.camp.jspcafe.repository.dto.UserUpdateRequest;
import woowa.camp.jspcafe.repository.user.DBUserRepository;
import woowa.camp.jspcafe.repository.user.InMemoryUserRepository;
import woowa.camp.jspcafe.repository.user.UserRepository;
import woowa.camp.jspcafe.service.dto.RegistrationRequest;
import woowa.camp.jspcafe.service.dto.UserResponse;
import woowa.camp.jspcafe.utils.FixedDateTimeProvider;

@ExtendWith(UserDBSetupExtension.class)
class UserServiceTest {

    DatabaseConnector connector = new DatabaseConnector();
    UserRepository userRepository = new DBUserRepository(connector);
    DateTimeProvider fixedDateTimeProvider = new FixedDateTimeProvider(2024, 7, 24);
    UserService userService = new UserService(userRepository, fixedDateTimeProvider);

    @Nested
    @DisplayName("Describe_회원가입 기능은")
    class RegistrationTest {

        @Test
        @DisplayName("[Success] 아이디, 비밀번호, 이름, 이메일 정보가 모두 있어야 회원가입을 성공한다")
        void registration() {
            RegistrationRequest registrationRequest = new RegistrationRequest("email.com", "닉네임", "비밀번호");
            assertThatCode(() -> userService.registration(registrationRequest))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("[Success] 현재 년/월/일 기준으로 회원가입된다")
        void registrationDate() {
            RegistrationRequest registrationRequest = new RegistrationRequest("email.com", "닉네임", "비밀번호");
            User registration = userService.registration(registrationRequest);

            LocalDate registerAt = registration.getRegisterAt();
            LocalDate expectedAt = fixedDateTimeProvider.getNow();
            assertThat(registerAt).isNotNull();
            assertThat(registerAt.getYear()).isEqualTo(expectedAt.getYear());
            assertThat(registerAt.getMonth()).isEqualTo(expectedAt.getMonth());
            assertThat(registerAt.getDayOfMonth()).isEqualTo(expectedAt.getDayOfMonth());
        }

        @ParameterizedTest(name = "[Exception] {0} 정보가 없으면 회원가입을 실패한다")
        @ValueSource(strings = {"비밀번호", "닉네임", "이메일"})
        void registrationFailureWhenMissingField(String fieldName) {
            RegistrationRequest registrationRequest = createRequestWithMissingField(fieldName);
            assertThatThrownBy(() -> userService.registration(registrationRequest))
                    .isInstanceOf(UserException.class);
        }

        @ParameterizedTest(name = "[Exception] {0} 정보가 빈값이면 회원가입을 실패한다")
        @ValueSource(strings = {"비밀번호", "닉네임", "이메일"})
        void registrationFailureWhenEmptyField(String fieldName) {
            RegistrationRequest registrationRequest = createRequestWithEmptyField(fieldName);
            assertThatThrownBy(() -> userService.registration(registrationRequest))
                    .isInstanceOf(UserException.class);
        }

        private RegistrationRequest createRequestWithMissingField(String fieldName) {
            return switch (fieldName) {
                case "비밀번호" -> new RegistrationRequest("email.com", "닉네임", null);
                case "닉네임" -> new RegistrationRequest("email.com", null, "비밀번호");
                case "이메일" -> new RegistrationRequest(null, "닉네임", "비밀번호");
                default -> throw new IllegalArgumentException("Unexpected field: " + fieldName);
            };
        }

        private RegistrationRequest createRequestWithEmptyField(String fieldName) {
            return switch (fieldName) {
                case "비밀번호" -> new RegistrationRequest("email.com", "닉네임", "");
                case "닉네임" -> new RegistrationRequest("email.com", "", "비밀번호");
                case "이메일" -> new RegistrationRequest("", "닉네임", "비밀번호");
                default -> throw new IllegalArgumentException("Unexpected field: " + fieldName);
            };
        }
    }

    @Nested
    @DisplayName("Describe_사용자 목록을 조회하는 기능은")
    class FindAllTest {

        DateTimeProvider fixedDateTimeProvider = new FixedDateTimeProvider(2024, 7, 24);
        UserRepository userRepository = new InMemoryUserRepository();
        UserService userService = new UserService(userRepository, fixedDateTimeProvider);

        @Test
        @DisplayName("[Success] 사용자 목록에 대한 UserResponse 리스트를 반환한다")
        void test() {
            User user1 = UserFixture.createUser1();
            User user2 = UserFixture.createUser2();
            userRepository.save(user1);
            userRepository.save(user2);
            List<UserResponse> result = userService.findAll();
            assertThat(result).size().isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Describe_사용자를 조회하는 기능은")
    class FindByIdTest {

        @Test
        @DisplayName("[Success] 회원가입한 사용자는 조회에 성공한다")
        void test1() {
            User user1 = UserFixture.createUser1();
            User registUser = userService.registration(
                    new RegistrationRequest(user1.getEmail(), user1.getNickname(), user1.getPassword()));
            User findUser = userService.findById(registUser.getId());

            assertThat(findUser).isEqualTo(registUser);
        }

        @Test
        @DisplayName("[Exception] 없는 사용자를 조회하면 예외가 발생한다")
        void test2() {
            assertThatThrownBy(() -> userService.findById(987654321L))
                    .isInstanceOf(UserException.class);
        }
    }

    @Nested
    @DisplayName("Describe_사용자 프로필을 수정하는 기능은")
    class UpdateUserProfileTest {


        @Test
        @DisplayName("[Success] 비밀번호가 일치하면 프로필 수정에 성공한다")
        void test() {
            // given
            User user = UserFixture.createUser(1, fixedDateTimeProvider.getNow());
            userRepository.save(user);
            String newPassword = "new password";
            String newNickname = "new nickname";
            UserUpdateRequest userUpdateRequest = new UserUpdateRequest(newPassword, newNickname);

            // when
            userService.updateUserProfile(user.getId(), user.getPassword(), userUpdateRequest);

            // then
            User findUser = userRepository.findById(user.getId()).orElseThrow();
            assertThat(findUser.getPassword()).isEqualTo(newPassword);
            assertThat(findUser.getNickname()).isEqualTo(newNickname);
        }

        @Test
        @DisplayName("[Exception] 비밀번호가 일치하지 않으면 UserException 예외를 반환한다")
        void test2() {
            // given
            User user = UserFixture.createUser(1, fixedDateTimeProvider.getNow());
            userRepository.save(user);
            String wrongPassword = "wrong password";
            String newPassword = "new password";
            String newNickname = "new nickname";
            UserUpdateRequest userUpdateRequest = new UserUpdateRequest(newPassword, newNickname);

            // when then
            assertThatThrownBy(() -> userService.updateUserProfile(user.getId(), wrongPassword, userUpdateRequest))
                    .isInstanceOf(UserException.class);
        }
    }

    @Nested
    @DisplayName("Describe_로그인하는 기능은")
    class LoginTest {

        @Test
        @DisplayName("[Success] 이메일과 비밀번호가 같으면 User를 반환한다")
        void sameEmailAndPassword() {
            // given
            User user = new User("email@naver.com", "닉네임", "password", fixedDateTimeProvider.getNow());
            userRepository.save(user);

            // when
            User loginUser = userService.login(user.getEmail(), user.getPassword());
            // then
            assertThat(user).isEqualTo(loginUser);
        }

        @Test
        @DisplayName("[Exception] 이메일이 다르면 UserException을 반환한다")
        void wrongEmail() {
            // given
            User user = new User("email@naver.com", "닉네임", "password", fixedDateTimeProvider.getNow());
            userRepository.save(user);

            // when then
            assertThatThrownBy(() -> userService.login("email1@naver.com", user.getPassword()))
                    .isInstanceOf(UserException.class);
        }

        @Test
        @DisplayName("[Exception] 비밀번호가 다르면 UserException을 반환한다")
        void wrongPassword() {
            // given
            User user = new User("email@naver.com", "닉네임", "password", fixedDateTimeProvider.getNow());
            userRepository.save(user);

            // when then
            assertThatThrownBy(() -> userService.login(user.getEmail(), "password!"))
                    .isInstanceOf(UserException.class);
        }

        @Test
        @DisplayName("[Exception] 아이디와 비밀번호가 다르면 UserException을 반환한다")
        void wrongEmailAndPassword() {
            // given
            User user = new User("email@naver.com", "닉네임", "password", fixedDateTimeProvider.getNow());
            userRepository.save(user);

            // when then
            assertThatThrownBy(() -> userService.login("emaill@naver.com", "password!"))
                    .isInstanceOf(UserException.class);
        }
    }

}