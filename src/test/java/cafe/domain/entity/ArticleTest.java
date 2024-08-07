package cafe.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {
    @Test
    void 올바른_인자가_들어간_객체를_생성한다() {
        // given
        String articleId = "articleId";
        String writer = "writer";
        String title = "title";
        String contents = "contents";

        // when
        Article article = Article.of(articleId, writer, title, contents);

        // then
        assertEquals(article.getArticleId(), articleId);
        assertEquals(article.getWriter(), writer);
        assertEquals(article.getTitle(), title);
        assertEquals(article.getContents(), contents);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidArticleInputs")
    void 인자에_null이_들어가면_오류가_발생한다(String articleId, String title, String contents, String author) {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () -> Article.of(articleId, title, contents, author));
    }

    private static Stream<Arguments> provideInvalidArticleInputs() {
        return Stream.of(
                Arguments.of(null, "test", "test", "test"),
                Arguments.of("test", null, "test", "test"),
                Arguments.of("test", "test", null, "test"),
                Arguments.of("test", "test", "test", null)
        );
    }
}