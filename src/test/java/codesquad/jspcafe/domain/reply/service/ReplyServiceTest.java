package codesquad.jspcafe.domain.reply.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import codesquad.jspcafe.common.utils.DateTimeFormatExecutor;
import codesquad.jspcafe.domain.reply.domain.Reply;
import codesquad.jspcafe.domain.reply.payload.request.ReplyCreateRequest;
import codesquad.jspcafe.domain.reply.payload.request.ReplyUpdateRequest;
import codesquad.jspcafe.domain.reply.payload.respose.ReplyCommonResponse;
import codesquad.jspcafe.domain.reply.repository.ReplyMemoryRepository;
import codesquad.jspcafe.domain.reply.repository.ReplyRepository;
import codesquad.jspcafe.domain.user.domain.User;
import codesquad.jspcafe.domain.user.repository.UserMemoryRepository;
import codesquad.jspcafe.domain.user.repository.UserRepository;
import java.lang.reflect.Field;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ReplyService는")
class ReplyServiceTest {

    private ReplyRepository replyRepository = new ReplyMemoryRepository();
    private final UserRepository userRepository = new UserMemoryRepository();
    private final ReplyService replyService = new ReplyService(userRepository, replyRepository);

    private final String expectedUserId = "userId";
    private final String expectedContents = "contents";
    private final User expectedWriter = new User(1L, expectedUserId, "password", "name",
        "test@gmail.com");
    private final LocalDateTime expectedCreatedAt = LocalDateTime.now();
    private final Long expectedId = 1L;
    private final Long expectedArticleId = 1L;
    private Reply expectedReply;

    {
        userRepository.save(expectedWriter);
    }

    @BeforeEach
    void clear() {
        replyRepository = new ReplyMemoryRepository();
        for (Field field : replyService.getClass().getDeclaredFields()) {
            if (field.getType() != ReplyRepository.class) {
                continue;
            }
            field.setAccessible(true);
            try {
                field.set(replyService, replyRepository);
            } catch (IllegalAccessException ignore) {
            }
        }
        expectedReply = new Reply(expectedId, expectedArticleId, expectedWriter, expectedContents,
            expectedCreatedAt);
    }

    @Test
    @DisplayName("댓글을 생성할 수 있다.")
    void createReply() {
        // Arrange
        Map<String, String[]> expectedValues = Map.of(
            "article", new String[]{String.valueOf(expectedArticleId)},
            "contents", new String[]{expectedContents}
        );
        // Act
        ReplyCommonResponse actualResult = replyService.createReply(
            ReplyCreateRequest.of(expectedValues, expectedUserId));
        // Assert
        assertAll(
            () -> assertThat(actualResult)
                .extracting("article", "userId", "username", "contents")
                .containsExactly(expectedArticleId, expectedUserId, expectedWriter.getUsername(),
                    expectedContents),
            () -> assertThat(actualResult.getId()).isNotNull(),
            () -> assertThat(actualResult.getCreatedAt()).isNotNull()
        );
        ;
    }

    @Nested
    @DisplayName("댓글을 조회할 때")
    class whenGetReplies {

        @Test
        @DisplayName("해당 게시글의 댓글을 조회할 수 있다.")
        void getRepliesByArticleId() {
            // Arrange
            replyRepository.save(expectedReply);
            // Act
            List<ReplyCommonResponse> actualResult = replyService.getRepliesByArticleId(
                expectedArticleId);
            // Assert
            assertAll(
                () -> assertThat(actualResult).hasSize(1),
                () -> assertThat(actualResult.get(0))
                    .extracting("id", "article", "userId", "username", "contents", "createdAt")
                    .containsExactly(expectedId, expectedArticleId, expectedUserId,
                        expectedWriter.getUsername(), expectedContents,
                        DateTimeFormatExecutor.execute(expectedCreatedAt))
            );
        }
    }

    @Nested
    @DisplayName("댓글을 수정할 때")
    class whenUpdateReply {

        @Test
        @DisplayName("댓글을 수정할 수 있다.")
        void updateReply() throws AccessDeniedException {
            // Arrange
            replyRepository.save(expectedReply);
            String updatedContents = "updatedContents";
            // Act
            ReplyCommonResponse actualResult = replyService.updateReply(new ReplyUpdateRequest(
                expectedId, expectedUserId, updatedContents));
            // Assert
            assertThat(actualResult)
                .extracting("id", "article", "userId", "username", "contents", "createdAt")
                .containsExactly(expectedId, expectedArticleId, expectedUserId,
                    expectedWriter.getUsername(), updatedContents,
                    DateTimeFormatExecutor.execute(expectedCreatedAt));
        }

        @Test
        @DisplayName("수정 권한이 없으면 예외를 던진다.")
        void updateReplyFailed() {
            // Arrange
            replyRepository.save(expectedReply);
            String updatedContents = "updatedContents";
            // Act & Assert
            assertThatThrownBy(() -> replyService.updateReply(new ReplyUpdateRequest(
                expectedId, "otherUserId", updatedContents)))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("수정 권한이 없습니다.");
        }

        @Test
        @DisplayName("댓글을 찾을 수 없으면 예외를 던진다.")
        void updateReplyFailedWhenNotFound() {
            // Arrange
            ReplyUpdateRequest request = new ReplyUpdateRequest(expectedId, expectedUserId,
                "updatedContents");
            // Act & Assert
            assertThatThrownBy(() -> replyService.updateReply(request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("댓글을 찾을 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("댓글을 삭제할 때")
    class whenDeleteReply {

        @Test
        @DisplayName("댓글을 삭제할 수 있다.")
        void deleteReply() throws AccessDeniedException {
            // Arrange
            replyRepository.save(expectedReply);
            // Act
            replyService.deleteReply(expectedId, expectedUserId);
            // Assert
            assertThat(replyRepository.findById(expectedId)).isEmpty();
        }

        @Test
        @DisplayName("삭제 권한이 없으면 예외를 던진다.")
        void deleteReplyFailed() {
            // Arrange
            replyRepository.save(expectedReply);
            // Act & Assert
            assertThatThrownBy(() -> replyService.deleteReply(expectedId, "otherUserId"))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("삭제 권한이 없습니다.");
        }

        @Test
        @DisplayName("댓글을 찾을 수 없으면 예외를 던진다.")
        void deleteReplyFailedWhenNotFound() {
            // Act & Assert
            assertThatThrownBy(() -> replyService.deleteReply(expectedId, expectedUserId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("댓글을 찾을 수 없습니다.");
        }
    }
}