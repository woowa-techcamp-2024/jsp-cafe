package codesquad.jspcafe.domain.reply.domain;

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

@DisplayName("Reply 도메인은")
class ReplyTest {

    private final String expectedContents = "contents";
    private final LocalDateTime expectedCreatedAt = LocalDateTime.now();
    private final User expectedWriter = new User(1L, "userId", "password", "name",
        "test@gmail.com");
    private final Long expectedArticle = 1L;


    @Nested
    @DisplayName("생성 시")
    class whenCreated {

        @Test
        @DisplayName("정상적으로 생성된다.")
        void createSuccess() {
            // Act
            Reply actualResult = new Reply(expectedArticle, expectedWriter, expectedContents,
                expectedCreatedAt);
            // Assert
            assertThat(actualResult)
                .extracting("article", "writer", "contents", "createdAt")
                .containsExactly(expectedArticle, expectedWriter, expectedContents,
                    expectedCreatedAt);
        }

        @Test
        @DisplayName("정상적으로 생성된다.")
        void createSuccessWithFull() {
            // Arrange
            Long expectedId = 1L;
            // Act
            Reply actualResult = new Reply(expectedId, expectedArticle, expectedWriter,
                expectedContents, expectedCreatedAt);
            // Assert
            assertThat(actualResult)
                .extracting("id", "article", "writer", "contents", "createdAt")
                .containsExactly(expectedId, expectedArticle, expectedWriter,
                    expectedContents,
                    expectedCreatedAt);
        }

        @Test
        @DisplayName("article이 null이면 예외가 발생한다.")
        void createFailWhenArticleIsNull() {
            // Act & Assert
            assertThatThrownBy(
                () -> new Reply(null, expectedWriter, expectedContents, expectedCreatedAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("게시글이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("writer가 null이면 예외가 발생한다.")
        void createFailWhenWriterIsNull() {
            // Act & Assert
            assertThatThrownBy(
                () -> new Reply(expectedArticle, null, expectedContents, expectedCreatedAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("작성자가 존재하지 않습니다.");
        }

        @ParameterizedTest
        @MethodSource("exceptionContents")
        @DisplayName("contents가 null이거나 빈 문자열이면 예외가 발생한다.")
        void createFailWhenContentsIsNull(String value) {
            // Act & Assert
            assertThatThrownBy(
                () -> new Reply(expectedArticle, expectedWriter, value, expectedCreatedAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글 내용이 비어있습니다.");
        }

        private static Stream<Arguments> exceptionContents() {
            return Stream.of(
                Arguments.of(""),
                Arguments.of(" "),
                Arguments.of((String) null)
            );
        }

    }

    @Nested
    @DisplayName("생성 후")
    class afterCreated {

        @Test
        @DisplayName("id를 수정할 수 있다.")
        void setId() {
            // Arrange
            Long expectedId = 1L;
            Reply reply = new Reply(expectedArticle, expectedWriter, expectedContents,
                expectedCreatedAt);
            // Act
            reply.setId(expectedId);
            // Assert
            assertThat(reply.getId()).isEqualTo(expectedId);
        }

    }

    @Nested
    @DisplayName("수정 시")
    class whenUpdated {

        @Test
        @DisplayName("정상적으로 수정된다.")
        void updateSuccess() {
            // Arrange
            String updatedContents = "updatedContents";
            Reply reply = new Reply(expectedArticle, expectedWriter, updatedContents,
                expectedCreatedAt);
            // Act
            reply.update(updatedContents);
            // Assert
            assertThat(reply.getContents()).isEqualTo(updatedContents);
        }

        @ParameterizedTest
        @MethodSource("exceptionContents")
        @DisplayName("contents가 null이거나 빈 문자열이면 예외가 발생한다.")
        void updateFailWhenContentsIsNull(String value) {
            // Arrange
            Reply reply = new Reply(expectedArticle, expectedWriter, expectedContents,
                expectedCreatedAt);
            // Act & Assert
            assertThatThrownBy(() -> reply.update(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글 내용이 비어있습니다.");
        }

        private static Stream<Arguments> exceptionContents() {
            return Stream.of(
                Arguments.of(""),
                Arguments.of(" "),
                Arguments.of((String) null)
            );
        }
    }

}