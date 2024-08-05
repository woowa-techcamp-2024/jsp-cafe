package codesquad.article.handler.dto.response;

public record CommentResponse(
        Long id,
        Long commenterId,
        String commenter,
        String content
) {
}
