package codesquad.jspcafe.domain.article.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import codesquad.jspcafe.common.utils.DateTimeFormatExecutor;
import codesquad.jspcafe.domain.article.domain.Article;
import codesquad.jspcafe.domain.article.payload.response.ArticleCommonResponse;
import codesquad.jspcafe.domain.article.payload.response.ArticleContentResponse;
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

    private final ArticleService articleService = new ArticleService();
    private ArticleRepository articleRepository;

    private final String expectedTitle = "title";
    private final String expectedWriter = "writer";
    private final String expectedContents = "contents";
    private final LocalDateTime expectedCreatedAt = LocalDateTime.now();

    @BeforeEach
    void clear() {
        articleRepository = new ArticleRepository();
        for (Field field : articleService.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                field.set(articleService, articleRepository);
            } catch (IllegalAccessException ignore) {
            }
        }
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
            // Act & Assert
            assertThatThrownBy(() -> articleService.getArticleByTitle(expectedTitle))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아티클이 존재하지 않습니다!");
        }

        @Test
        @DisplayName("아티클이 존재하면 아티클을 반환한다.")
        void getArticleByTitleSuccess() {
            // Arrange
            articleRepository.save(
                new Article(expectedTitle, expectedWriter, expectedContents, expectedCreatedAt));
            // Act
            ArticleCommonResponse actualResult = articleService.getArticleByTitle(expectedTitle);
            // Assert
            assertThat(actualResult)
                .extracting("title", "writer", "contents", "createdAt")
                .containsExactly(expectedTitle, expectedWriter, expectedContents,
                    DateTimeFormatExecutor.execute(expectedCreatedAt));
        }

        @Test
        @DisplayName("모든 아티클을 조회할 수 있다.")
        void findAllArticle() {
            // Arrange
            articleRepository.save(
                new Article(expectedTitle, expectedWriter, expectedContents, expectedCreatedAt));
            // Act
            List<ArticleContentResponse> actualResult = articleService.findAllArticle();
            // Assert
            assertAll(
                () -> assertThat(actualResult).hasSize(1),
                () -> assertThat(actualResult.get(0))
                    .extracting("title", "writer", "createdAt")
                    .containsExactly(expectedTitle, expectedWriter,
                        DateTimeFormatExecutor.execute(expectedCreatedAt))
            );
        }
    }
}