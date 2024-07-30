package codesquad.jspcafe.domain.article.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import codesquad.jspcafe.domain.user.domain.User;
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
    private final User expectedWriter = new User(1L, "userId", "password", "name",
        "test@gmail.com");
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

        @DisplayName("정상적으로 생성된다.")
        @Test
        void createSuccessWithFull() {
            // Arrange
            Long expectedId = 1L;
            // Act
            Article actualResult = new Article(expectedId, expectedTitle, expectedWriter,
                expectedContents, expectedCreatedAt);
            // Assert
            assertThat(actualResult)
                .extracting("id", "title", "writer", "contents", "createdAt")
                .containsExactly(expectedId, expectedTitle, expectedWriter, expectedContents,
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

        @DisplayName("writer가 null이면 예외가 발생한다.")
        @Test
        void createFailWhenWriterIsNull() {
            // Act & Assert
            assertThatThrownBy(
                () -> new Article(expectedTitle, null, expectedContents, expectedCreatedAt))
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

    @Nested
    @DisplayName("생성 이후")
    class afterCreated {

        @Test
        @DisplayName("id를 추가할 수 있다.")
        void setIdAfterCreated() {
            // Arrange
            Long expectedId = 1L;
            Article article = new Article(expectedTitle, expectedWriter, expectedContents,
                expectedCreatedAt);
            // Act
            article.setId(expectedId);
            // Assert
            assertThat(article.getId()).isEqualTo(expectedId);

        }

        @Test
        @DisplayName("title과 contents를 수정할 수 있다.")
        void updateValues() {
            // Arrange
            String updatedTitle = "new title";
            String updatedContent = "new contents";
            Article article = new Article(updatedTitle, expectedWriter, updatedContent,
                expectedCreatedAt);
            // Act
            article.updateValues(updatedTitle, updatedContent);
            // Assert
            assertThat(article)
                .extracting("title", "contents")
                .containsExactly(updatedTitle, updatedContent);
        }

        @Nested
        @DisplayName("수정 시")
        class whenUpdate {

            @DisplayName("title이 null이거나 빈 문자열이면 예외가 발생한다.")
            @MethodSource("expectedValues")
            @ParameterizedTest
            void updateValuesFailWhenTitleIsNull(String value) {
                // Arrange
                Article article = new Article(expectedTitle, expectedWriter, expectedContents,
                    expectedCreatedAt);
                // Act & Assert
                assertThatThrownBy(() -> article.updateValues(value, expectedContents))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("제목은 필수 입력값입니다.");
            }

            @DisplayName("contents가 null이거나 빈 문자열이면 예외가 발생한다.")
            @MethodSource("expectedValues")
            @ParameterizedTest
            void updateValuesFailWhenContentsIsNull(String value) {
                // Arrange
                Article article = new Article(expectedTitle, expectedWriter, expectedContents,
                    expectedCreatedAt);
                // Act & Assert
                assertThatThrownBy(() -> article.updateValues(expectedTitle, value))
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

}