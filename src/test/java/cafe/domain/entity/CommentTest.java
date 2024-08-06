package cafe.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {
    @Test
    void 올바른_인자가_들어간_객체를_생성한다() {
        // given
        String commentId = "commentId";
        String userId = "userId";
        String articleId = "articleId";
        String contents = "contents";

        // when
        Comment comment = Comment.of(commentId, userId, articleId, contents);

        // then
        assertEquals(commentId, comment.getCommentId());
        assertEquals(userId, comment.getUserId());
        assertEquals(articleId, comment.getArticleId());
        assertEquals(contents, comment.getContents());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidComment")
    void 인자에_null이_있으면_예외가_발생한다(String commentId, String userId, String articleId, String contents) {
        // given, when, then
        assertThrows(IllegalArgumentException.class, () -> Comment.of(commentId, userId, articleId, contents));
    }

    private static Stream<Arguments> provideInvalidComment() {
        return Stream.of(
                Arguments.of(null, "userId", "articleId", "contents"),
                Arguments.of("commentId", null, "articleId", "contents"),
                Arguments.of("commentId", "userId", null, "contents"),
                Arguments.of("commentId", "userId", "articleId", null)
        );
    }
}