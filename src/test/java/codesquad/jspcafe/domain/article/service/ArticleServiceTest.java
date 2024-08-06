package codesquad.jspcafe.domain.article.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import codesquad.jspcafe.common.utils.DateTimeFormatExecutor;
import codesquad.jspcafe.domain.article.domain.Article;
import codesquad.jspcafe.domain.article.payload.request.ArticleUpdateRequest;
import codesquad.jspcafe.domain.article.payload.response.ArticleCommonResponse;
import codesquad.jspcafe.domain.article.repository.ArticleMemoryRepository;
import codesquad.jspcafe.domain.article.repository.ArticleRepository;
import codesquad.jspcafe.domain.reply.domain.Reply;
import codesquad.jspcafe.domain.reply.repository.ReplyMemoryRepository;
import codesquad.jspcafe.domain.reply.repository.ReplyRepository;
import codesquad.jspcafe.domain.user.domain.User;
import codesquad.jspcafe.domain.user.repository.UserMemoryRepository;
import codesquad.jspcafe.domain.user.repository.UserRepository;
import java.lang.reflect.Field;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ArticleService는")
class ArticleServiceTest {

    private ArticleRepository articleRepository = new ArticleMemoryRepository();
    private ReplyRepository replyRepository = new ReplyMemoryRepository();
    private UserRepository userRepository = new UserMemoryRepository();
    private final ArticleService articleService = new ArticleService(articleRepository,
        replyRepository, userRepository);

    private final String expectedUserId = "userId";
    private final String expectedTitle = "title";
    private final User expectedWriter1 = new User(1L, expectedUserId, "password", "name",
        "test@gmail.com");
    private final User expectedWriter2 = new User(2L, "userId2", "password", "name2",
        "test@gmail.com");
    private final String expectedContents = "contents";
    private final LocalDateTime expectedCreatedAt = LocalDateTime.now();
    private final Long expectedId = 1L;
    private Article expectedArticle;

    {
        userRepository.save(expectedWriter1);
        userRepository.save(expectedWriter2);
    }

    @BeforeEach
    void clear() {
        articleRepository = new ArticleMemoryRepository();
        replyRepository = new ReplyMemoryRepository();
        for (Field field : articleService.getClass().getDeclaredFields()) {
            if (field.getType() == ArticleRepository.class) {
                field.setAccessible(true);
                try {
                    field.set(articleService, articleRepository);
                } catch (IllegalAccessException ignore) {
                }
            }
            if (field.getType() == ReplyRepository.class) {
                field.setAccessible(true);
                try {
                    field.set(articleService, replyRepository);
                } catch (IllegalAccessException ignore) {
                }
            }
        }
        expectedArticle = new Article(expectedTitle, expectedWriter1, expectedContents,
            expectedCreatedAt);
        expectedArticle.setId(expectedId);
    }

    @Test
    @DisplayName("아티클을 생성할 수 있다.")
    void createArticle() {
        // Arrange
        Map<String, String[]> expectedValues = Map.of(
            "title", new String[]{expectedTitle},
            "contents", new String[]{expectedContents}
        );
        // Act
        ArticleCommonResponse actualResult = articleService.createArticle(expectedValues,
            expectedUserId);
        // Assert
        assertAll(
            () -> assertThat(actualResult.getId()).isNotNull(),
            () -> assertThat(actualResult)
                .extracting("title", "writerUserId", "writerUsername", "contents")
                .containsExactly(expectedTitle, expectedWriter1.getUserId(),
                    expectedWriter1.getUsername(), expectedContents),
            () -> assertThat(actualResult.getCreatedAt()).isNotNull()
        );
    }

    @Nested
    @DisplayName("아티클을 조회할 때")
    class whenGetArticle {

        @Test
        @DisplayName("아티클이 존재하지 않으면 예외를 던진다.")
        void getArticleByTitleFailed() {
            String expectedIdStr = Long.toString(expectedId);
            // Act & Assert
            assertThatThrownBy(() -> articleService.getArticleById(expectedIdStr))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아티클이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("아티클이 존재하면 아티클을 반환한다.")
        void getArticleByTitleSuccess() {
            // Arrange
            articleRepository.save(expectedArticle);
            // Act
            ArticleCommonResponse actualResult = articleService.getArticleById(
                expectedId.toString());
            // Assert
            assertThat(actualResult)
                .extracting("id", "title", "writerUserId", "writerUsername", "contents",
                    "createdAt")
                .containsExactly(expectedId, expectedTitle, expectedWriter1.getUserId(),
                    expectedWriter1.getUsername(), expectedContents,
                    DateTimeFormatExecutor.execute(expectedCreatedAt));
        }

    }

    @Nested
    @DisplayName("아티클을 수정할 때")
    class whenUpdateArticle {

        private final Map<String, String> expectedValues = Map.of(
            "id", String.valueOf(expectedId),
            "title", expectedContents,
            "contents", expectedContents
        );

        @Test
        @DisplayName("아티클이 존재하지 않으면 예외를 던진다.")
        void updateArticleFailed() {
            // Arrange
            ArticleUpdateRequest request = ArticleUpdateRequest.of(expectedValues, 1L);
            // Act & Assert
            assertThatThrownBy(() -> articleService.updateArticle(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아티클이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("아티클이 존재하면 아티클을 수정한다.")
        void updateArticleSuccess() throws AccessDeniedException {
            // Arrange
            articleRepository.save(expectedArticle);
            ArticleUpdateRequest request = ArticleUpdateRequest.of(expectedValues, 1L);
            // Act
            ArticleCommonResponse actualResult = articleService.updateArticle(request);
            // Assert
            assertAll(
                () -> assertThat(actualResult)
                    .extracting("id", "title", "writerUserId", "writerUsername", "contents",
                        "createdAt")
                    .containsExactly(expectedId, expectedContents, expectedWriter1.getUserId(),
                        expectedWriter1.getUsername(), expectedContents,
                        DateTimeFormatExecutor.execute(expectedCreatedAt))
            );
        }

        @Test
        @DisplayName("수정 권한이 없으면 예외를 던진다.")
        void updateArticleAccessDenied() {
            // Arrange
            articleRepository.save(expectedArticle);
            ArticleUpdateRequest request = ArticleUpdateRequest.of(expectedValues, 2L);
            // Act & Assert
            assertThatThrownBy(() -> articleService.updateArticle(request))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("수정 권한이 없습니다.");
        }
    }

    @Nested
    @DisplayName("아티클을 삭제할 때")
    class whenDeleteArticle {

        @Test
        @DisplayName("아티클이 존재하지 않으면 예외를 던진다.")
        void deleteArticleFailed() {
            // Act & Assert
            assertThatThrownBy(() -> articleService.deleteArticle(expectedId, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아티클이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("삭제 권한이 없으면 예외를 던진다.")
        void deleteArticleAccessDenied() {
            // Arrange
            articleRepository.save(expectedArticle);
            // Act & Assert
            assertThatThrownBy(() -> articleService.deleteArticle(expectedId, 2L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("삭제 권한이 없습니다.");
        }

        @Test
        @DisplayName("아티클을 삭제한다.")
        void deleteArticleSuccess() throws AccessDeniedException {
            // Arrange
            articleRepository.save(expectedArticle);
            // Act
            articleService.deleteArticle(expectedId, 1L);
            // Assert
            assertThat(articleRepository.findById(expectedId)).isEmpty();
        }

        @Test
        @DisplayName("아티클을 삭제할 때 다른 사용자의 댓글이 존재하면 예외를 던진다.")
        void deleteArticleWithOtherUserReply() {
            // Arrange
            articleRepository.save(expectedArticle);
            replyRepository.save(
                new Reply(expectedArticle.getId(), expectedWriter2, "contents", expectedCreatedAt));
            // Act & Assert
            assertThatThrownBy(() -> articleService.deleteArticle(expectedId, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("댓글이 존재하여 삭제할 수 없습니다.");
        }
    }

}