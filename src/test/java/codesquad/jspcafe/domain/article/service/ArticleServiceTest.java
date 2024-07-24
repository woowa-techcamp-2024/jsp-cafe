package codesquad.jspcafe.domain.article.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import codesquad.jspcafe.common.utils.DateTimeFormatExecutor;
import codesquad.jspcafe.domain.article.domain.Article;
import codesquad.jspcafe.domain.article.payload.response.ArticleCommonResponse;
import codesquad.jspcafe.domain.article.payload.response.ArticleContentResponse;
import codesquad.jspcafe.domain.article.repository.ArticleMemoryRepository;
import codesquad.jspcafe.domain.article.repository.ArticleRepository;
import java.lang.reflect.Field;
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
    private final ArticleService articleService = new ArticleService(articleRepository);

    private final String expectedTitle = "title";
    private final String expectedWriter = "writer";
    private final String expectedContents = "contents";
    private final LocalDateTime expectedCreatedAt = LocalDateTime.now();
    private final Long expectedId = 1L;
    private Article expectedArticle;

    @BeforeEach
    void clear() {
        articleRepository = new ArticleMemoryRepository();
        for (Field field : articleService.getClass().getDeclaredFields()) {
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
            "writer", new String[]{expectedWriter},
            "contents", new String[]{expectedContents}
        );
        // Act
        ArticleCommonResponse actualResult = articleService.createArticle(expectedValues);
        // Assert
        assertAll(
            () -> assertThat(actualResult.getId()).isNotNull(),
            () -> assertThat(actualResult)
                .extracting("title", "writer", "contents")
                .containsExactly(expectedTitle, expectedWriter, expectedContents),
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
                .hasMessage("아티클이 존재하지 않습니다!");
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
                .extracting("id", "title", "writer", "contents", "createdAt")
                .containsExactly(expectedId, expectedTitle, expectedWriter, expectedContents,
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
                    .extracting("id", "title", "writer", "createdAt")
                    .containsExactly(expectedId, expectedTitle, expectedWriter,
                        DateTimeFormatExecutor.execute(expectedCreatedAt))
            );
        }
    }
}