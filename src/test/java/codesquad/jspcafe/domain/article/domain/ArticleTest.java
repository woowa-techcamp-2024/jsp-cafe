package codesquad.jspcafe.domain.article.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Article 도메인은")
class ArticleTest {

    private final String expectedTitle = "title";
    private final String expectedWriter = "writer";
    private final String expectedContents = "contents";
    private final LocalDateTime expectedCreatedAt = LocalDateTime.now();

    @Nested
    @DisplayName("생성 시")
    class whenCreated {

        @DisplayName("정상적으로 생성된다.")
        @Test
        void createSuccess() {
            // Act
            Article actualResult = new Article(expectedTitle, expectedWriter, expectedContents,
                expectedCreatedAt);
            // Assert
            assertThat(actualResult)
                .extracting("title", "writer", "contents", "createdAt")
                .containsExactly(expectedTitle, expectedWriter, expectedContents,
                    expectedCreatedAt);
        }

        @DisplayName("title이 null이거나 빈 문자열이면 예외가 발생한다.")
        @MethodSource("expectedValues")
        @ParameterizedTest
        void createFailWhenTitleIsNull(String value) {
            // Act & Assert
            assertThatThrownBy(
                () -> new Article(value, expectedWriter, expectedContents, expectedCreatedAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목은 필수 입력값입니다.");
        }

        @DisplayName("writer가 null이거나 빈 문자열이면 예외가 발생한다.")
        @MethodSource("expectedValues")
        @ParameterizedTest
        void createFailWhenWriterIsNull(String value) {
            // Act & Assert
            assertThatThrownBy(
                () -> new Article(expectedTitle, value, expectedContents, expectedCreatedAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("글쓴이는 필수 입력값입니다.");
        }

        @DisplayName("contents가 null이거나 빈 문자열이면 예외가 발생한다.")
        @MethodSource("expectedValues")
        @ParameterizedTest
        void createFailWhenContentsIsNull(String value) {
            // Act & Assert
            assertThatThrownBy(
                () -> new Article(expectedTitle, expectedWriter, value, expectedCreatedAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("본문은 필수 입력값입니다.");
        }

        private static Stream<Arguments> expectedValues() {
            return Stream.of(
                Arguments.of((Object) null),
                Arguments.of("")
            );
        }
    }

}