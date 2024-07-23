package codesquad.jspcafe.domain.user.domain.values;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Email VO는")
class EmailTest {

    private final String expectedEmail = "test@jspcafe.com";

    @Nested
    @DisplayName("생성 시")
    class whenCreated {

        @Test
        @DisplayName("정상적으로 생성된다.")
        void createSuccess() {
            // Act
            Email actualResult = Email.from(expectedEmail);
            // Assert
            assertThat(actualResult.getValue()).isEqualTo(expectedEmail);
        }

        @DisplayName("이메일 형식이 올바르지 않은 경우 예외를 던진다.")
        @MethodSource
        @ParameterizedTest
        void createFail(String email) {
            // Act & Assert
            assertThatThrownBy(() -> Email.from(email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이메일 형식이 올바르지 않습니다.");
        }

        private static Stream<Arguments> createFail() {
            return Stream.of(
                Arguments.of("codesquad"),
                Arguments.of("codesquad@"),
                Arguments.of("codesquad.com"),
                Arguments.of("codesquad@com"),
                Arguments.of(""),
                Arguments.of((String) null));
        }

    }

    @Test
    @DisplayName("값이 동일하면 같은 객체로 판단한다.")
    void equals() {
        // Arrange
        Email expectedEmailVO1 = Email.from(expectedEmail);
        Email expectedEmailVO2 = Email.from(expectedEmail);
        // Act & Assert
        assertAll(
            () -> assertThat(expectedEmailVO1).isEqualTo(expectedEmailVO2),
            () -> assertThat(expectedEmailVO1).hasSameHashCodeAs(expectedEmailVO2)
        );
    }

}