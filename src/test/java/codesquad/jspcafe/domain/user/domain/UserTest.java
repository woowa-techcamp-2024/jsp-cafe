package codesquad.jspcafe.domain.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codesquad.jspcafe.domain.user.domain.values.Email;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("User 도메인은")
class UserTest {

    private final String expectedUserId = "testId";
    private final String expectedPassword = "testPassword";
    private final String expectedUsername = "testUsername";
    private final String expectedEmail = "test@gmail.com";

    @Nested
    @DisplayName("생성 시")
    class WhenCreated {

        @DisplayName("정상적으로 생성된다.")
        @Test
        void createSuccess() {
            // Act
            User actualResult = new User(expectedUserId, expectedPassword, expectedUsername,
                expectedEmail);
            // Assert
            assertThat(actualResult)
                .extracting("userId", "password", "username", "email")
                .containsExactly(expectedUserId, expectedPassword, expectedUsername,
                    Email.from(expectedEmail));
        }

        @DisplayName("userId가 null이거나 빈 문자열이면 예외가 발생한다.")
        @MethodSource("exceptedValues")
        @ParameterizedTest
        void createFailWhenUserIdIsNull(String value) {
            // Act & Assert
            assertThatThrownBy(
                () -> new User(value, expectedPassword, expectedUsername, expectedEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("userId should not null");
        }

        @DisplayName("password가 null이거나 빈 문자열이면 예외가 발생한다.")
        @MethodSource("exceptedValues")
        @ParameterizedTest
        void createFailWhenPasswordIsNull(String value) {
            // Act & Assert
            assertThatThrownBy(
                () -> new User(expectedUserId, value, expectedUsername, expectedEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("password should not null");
        }

        @DisplayName("username이 null이거나 빈 문자열이면 예외가 발생한다.")
        @MethodSource("exceptedValues")
        @ParameterizedTest
        void createFailWhenUsernameIsNull(String value) {
            // Act & Assert
            assertThatThrownBy(
                () -> new User(expectedUserId, expectedPassword, value, expectedEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("username should not null");
        }

        @DisplayName("email이 이메일 형식이 아니면 예외가 발생한다.")
        @MethodSource("exceptedValues")
        @ParameterizedTest
        void createFailWhenEmailIsNotEmailFormat(String value) {
            // Act & Assert
            assertThatThrownBy(
                () -> new User(expectedUserId, expectedPassword, expectedUsername, value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일 형식이 올바르지 않습니다.");
        }

        private static Stream<Arguments> exceptedValues() {
            return Stream.of(
                Arguments.of((String) null),
                Arguments.of("")
            );
        }
    }

    @Nested
    @DisplayName("비밀번호를 검증할 때")
    class whenVerifyPassword {

        @DisplayName("비밀번호가 일치하면 true를 반환한다.")
        @Test
        void verifyPasswordSuccess() {
            // Arrange
            User user = new User(expectedUserId, expectedPassword, expectedUsername, expectedEmail);
            // Act
            boolean actualResult = user.verifyPassword(expectedPassword);
            // Assert
            assertThat(actualResult).isTrue();
        }

        @DisplayName("비밀번호가 일치하지 않으면 false를 반환한다.")
        @Test
        void verifyPasswordFailed() {
            // Arrange
            User user = new User(expectedUserId, expectedPassword, expectedUsername, expectedEmail);
            // Act
            boolean actualResult = user.verifyPassword("wrongPassword");
            // Assert
            assertThat(actualResult).isFalse();
        }
    }

    @Nested
    @DisplayName("유저 정보를 업데이트할 때")
    class whenUpdateUserInfo {

        @DisplayName("유저 이름이 null이거나 빈 문자열이면 업데이트되지 않는다.")
        @MethodSource("exceptedValues")
        @ParameterizedTest
        void updateUserInfoFailedWhenUsernameIsNull(String value) {
            // Arrange
            User user = new User(expectedUserId, expectedPassword, expectedUsername, expectedEmail);
            // Act
            user.updateValues(value, expectedEmail);
            // Assert
            assertThat(user.getUsername()).isEqualTo(expectedUsername);
        }

        @DisplayName("이메일이 null이거나 빈 문자열이면 업데이트되지 않는다.")
        @MethodSource("exceptedValues")
        @ParameterizedTest
        void updateUserInfoFailedWhenEmailIsNull(String value) {
            // Arrange
            User user = new User(expectedUserId, expectedPassword, expectedUsername, expectedEmail);
            // Act
            user.updateValues(expectedUsername, value);
            // Assert
            assertThat(user.getEmail()).isEqualTo(Email.from(expectedEmail));
        }

        @DisplayName("유저 이름이 변경된다.")
        @Test
        void updateUserInfoSuccessWhenUsernameChanged() {
            // Arrange
            User user = new User(expectedUserId, expectedPassword, expectedUsername, expectedEmail);
            String changedUsername = "changedUsername";
            // Act
            user.updateValues(changedUsername, null);
            // Assert
            assertThat(user.getUsername()).isEqualTo(changedUsername);
        }

        @DisplayName("이메일이 변경된다.")
        @Test
        void updateUserInfoSuccessWhenEmailChanged() {
            // Arrange
            User user = new User(expectedUserId, expectedPassword, expectedUsername, expectedEmail);
            String changedEmail = "test1@gmail.com";
            // Act
            user.updateValues(null, changedEmail);
            // Assert
            assertThat(user.getEmail()).isEqualTo(Email.from(changedEmail));
        }

        private static Stream<Arguments> exceptedValues() {
            return Stream.of(
                Arguments.of((String) null),
                Arguments.of("")
            );
        }
    }

}