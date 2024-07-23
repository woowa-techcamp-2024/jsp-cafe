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

}