package codesquad.jspcafe.domain.article.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import codesquad.jspcafe.common.utils.DateTimeFormatExecutor;
import codesquad.jspcafe.domain.article.domain.Article;
import codesquad.jspcafe.domain.article.payload.request.ArticleUpdateRequest;
import codesquad.jspcafe.domain.article.payload.response.ArticleCommonResponse;
import codesquad.jspcafe.domain.article.payload.response.ArticleContentResponse;
import codesquad.jspcafe.domain.article.repository.ArticleMemoryRepository;
import codesquad.jspcafe.domain.article.repository.ArticleRepository;
import codesquad.jspcafe.domain.user.domain.User;
import codesquad.jspcafe.domain.user.repository.UserMemoryRepository;
import codesquad.jspcafe.domain.user.repository.UserRepository;
import java.lang.reflect.Field;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ArticleService는")
class ArticleServiceTest {

    private ArticleRepository articleRepository = new ArticleMemoryRepository();
    private UserRepository userRepository = new UserMemoryRepository();
    private final ArticleService articleService = new ArticleService(articleRepository,
        userRepository);

    private final String expectedUserId = "userId";
    private final String expectedTitle = "title";
    private final User expectedWriter = new User(1L, expectedUserId, "password", "name",
        "test@gmail.com");
    private final String expectedContents = "contents";
    private final LocalDateTime expectedCreatedAt = LocalDateTime.now();
    private final Long expectedId = 1L;
    private Article expectedArticle;

    {
        userRepository.save(expectedWriter);
    }

    @BeforeEach
    void clear() {
        articleRepository = new ArticleMemoryRepository();
        for (Field field : articleService.getClass().getDeclaredFields()) {
            if (field.getType() != ArticleRepository.class) {
                continue;
            }
            field.setAccessible(true);
            try {
                field.set(articleService, articleRepository);
            } catch (IllegalAccessException ignore) {
            }
        }
        expectedArticle = new Article(expectedTitle, expectedWriter, expectedContents,
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
                .containsExactly(expectedTitle, expectedWriter.getUserId(),
                    expectedWriter.getUsername(), expectedContents),
            () -> assertThat(actualResult.getCreatedAt()).isNotNull()
        );
    }

    @Nested
    @DisplayName("아티클을 조회할 때")
    class whenGetArticle {

        @Test
        @DisplayName("아티클이 존재하지 않으면 에외를 던진다.")
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
                .containsExactly(expectedId, expectedTitle, expectedWriter.getUserId(),
                    expectedWriter.getUsername(), expectedContents,
                    DateTimeFormatExecutor.execute(expectedCreatedAt));
        }

        @Test
        @DisplayName("모든 아티클을 조회할 수 있다.")
        void findAllArticle() {
            // Arrange
            articleRepository.save(expectedArticle);
            // Act
            List<ArticleContentResponse> actualResult = articleService.findAllArticle();
            // Assert
            assertAll(
                () -> assertThat(actualResult).hasSize(1),
                () -> assertThat(actualResult.get(0))
                    .extracting("id", "title", "writerUserId", "writerUsername", "createdAt")
                    .containsExactly(expectedId, expectedTitle, expectedWriter.getUserId(),
                        expectedWriter.getUsername(),
                        DateTimeFormatExecutor.execute(expectedCreatedAt))
            );
        }
    }

    @Nested
    @DisplayName("아티클을 수정할 때")
    class whenUpdateArticle {

        private final String expectedUpdateTitle = "updateTitle";
        private final String expectedUpdateContents = "updateContents";

        private final Map<String, String> expectedValues = Map.of(
            "id", String.valueOf(expectedId),
            "title", expectedContents,
            "contents", expectedContents
        );

        @Test
        @DisplayName("아티클이 존재하지 않으면 에외를 던진다.")
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
                    .containsExactly(expectedId, expectedContents, expectedWriter.getUserId(),
                        expectedWriter.getUsername(), expectedContents,
                        DateTimeFormatExecutor.execute(expectedCreatedAt))
            );
        }

        @Test
        @DisplayName("수정 권한이 없으면 에외를 던진다.")
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
}